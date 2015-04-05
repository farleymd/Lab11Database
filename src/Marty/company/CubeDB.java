package Marty.company;

import java.sql.*;
import java.util.Scanner;

/**
 * Created by marty.farley on 4/2/2015.
 */
public class CubeDB {

    private static String protocol = "jdbc:derby:";
    private static String dbName = "cubeDB";

    private static final String USER = "username";
    private static final String PASS = "password";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Statement statement = null;
        Connection conn = null;
        ResultSet resultSet = null;

        try {
            conn = DriverManager.getConnection(protocol + dbName
                    + ";create=true", USER, PASS);
            statement = conn.createStatement();

            String createCubeSQL = "CREATE TABLE Cubes (SolveID int, " +
                    "Solver varchar(60), TimeTaken float)";
            statement.executeUpdate(createCubeSQL);
            System.out.println("Table created");

        } catch (SQLException se) {
            se.printStackTrace();
        }

        String[] allSolvers = {"Cubestormer 2 robot",
                "Fakhir Raihaan (using his feet)", "Ruxin Liu (age 3)",
                "Mats Valk (human record holder"};
        float[] allRecords = {5.270f, 27.93f, 99.33f, 6.27f};

        //TODO FIGURE OUT WHY ROMAN NUMERALS CAUSE ERROR

        for (int i = 0; i < allSolvers.length; i++) {

            String insertSQLStatement = "INSERT INTO Cubes VALUES (" + i + ", '" + allSolvers[i]
                    + "', " + allRecords[i] + ")";
            //System.out.println(insertSQLStatement);

            try {
                statement.executeUpdate(insertSQLStatement);
            } catch (SQLException se){
                se.printStackTrace();
            }
        }

        String fetchAllDataSQL = "SELECT * FROM Cubes";

        int lastDBID = 0;

        try {
            resultSet = statement.executeQuery(fetchAllDataSQL);
            while (resultSet.next()){
                int holderID = resultSet.getInt("SolveID");
                String recordHolder = resultSet.getString("Solver");
                float recordTime = resultSet.getFloat("TimeTaken");
                lastDBID = holderID;

                System.out.println("SolveID: " + holderID + " Thing that can solve a rubik's cube: "
                        + recordHolder + " Time taken, in seconds: " + recordTime);

            }
        } catch (SQLException se){
            se.printStackTrace();
        }

        System.out.println("Would you like to add a new record? Y/N");
        String userAnswer = scanner.nextLine();

        if (userAnswer.equalsIgnoreCase("Y")){
            System.out.println("What is the thing that solved the cube?");
            String solver = scanner.nextLine();
            System.out.println("What was the time it took, in seconds?");
            String timeInput = scanner.nextLine();
            float time = Float.parseFloat(timeInput);
            lastDBID = lastDBID + 1;

            String insertSQLStatement = "INSERT INTO Cubes VALUES (" + lastDBID + ", '" + solver
                    + "', " + time + ")";
            try {
                statement.executeUpdate(insertSQLStatement);
                try {
                    resultSet = statement.executeQuery(fetchAllDataSQL);
                    while (resultSet.next()){
                        int holderID = resultSet.getInt("SolveID");
                        String recordHolder = resultSet.getString("Solver");
                        float recordTime = resultSet.getFloat("TimeTaken");
                        lastDBID = holderID;

                        System.out.println("SolveID: " + holderID + " Thing that can solve a rubik's cube: "
                                + recordHolder + " Time taken, in seconds: " + recordTime);

                    }
                } catch (SQLException se){
                    se.printStackTrace();
                }
            } catch (SQLException se){
                se.printStackTrace();
            }
        }


        String deleteCubeSQL = "DROP TABLE cubes";
        try {
            statement.executeUpdate(deleteCubeSQL);
        } catch (SQLException se){
            se.printStackTrace();
        }


        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException se){
            se.printStackTrace();
        }

        try {
            if (conn != null){
                conn.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }


}
