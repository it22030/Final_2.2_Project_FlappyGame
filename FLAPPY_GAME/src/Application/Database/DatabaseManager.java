package Application.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private final String URL = "jdbc:mysql://localhost:3306/flappybird";
    private final String USER = "root";
    private final String PASSWORD = "ashik";

    public DatabaseManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveScore(String playerName, int score) {
        String sql = "INSERT INTO scores (player_name, score) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, playerName);
            stmt.setInt(2, score);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getHighestScore() {
        String sql = "SELECT MAX(score) FROM scores";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<String> getTopScores(int limit) {
        List<String> scores = new ArrayList<>();
        String sql = "SELECT player_name, score FROM scores ORDER BY score DESC LIMIT ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                scores.add(rs.getString("player_name") + " - " + rs.getInt("score"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scores;
    }
}
