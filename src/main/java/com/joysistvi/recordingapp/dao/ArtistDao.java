package com.joysistvi.recordingapp.dao;

import com.joysistvi.recordingapp.config.DbConnection;

import java.sql.*;

public class ArtistDao {

    private final DbConnection dbConnection;

    public ArtistDao(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    // =========================
    // READ ALL ARTISTS
    // =========================
    public void readAllArtists() {
        String query = "SELECT * FROM artists WHERE is_archived = 0";

        try (Connection conn = dbConnection.connect();
             Statement stmnt = conn.createStatement();
             ResultSet result = stmnt.executeQuery(query)) {

            System.out.println("+-------+----------------------+");
            System.out.printf("| %-5s | %-20s |%n", "ID", "Name");
            System.out.println("+-------+----------------------+");

            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");

                System.out.printf("| %-5d | %-20s |%n", id, name);
            }

            System.out.println("+-------+----------------------+");

        } catch (SQLException e) {
            System.err.println("Get All Artists: " + e.getMessage());
        }
    }

    // =========================
    // CREATE ARTIST
    // =========================
    public void createArtist(String name) {

        if (name == null || name.trim().isEmpty()) {
            System.out.println("Artist name is required.");
            return;
        }

        String query = "INSERT INTO artists (name) VALUES (?)";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, name);

            int rows = prep.executeUpdate();

            System.out.println(
                    rows > 0
                            ? "Artist " + name + " added successfully.\n"
                            : "Failed to add artist."
            );

            readAllArtists();

        } catch (SQLException e) {
            System.err.println("Create Artist: " + e.getMessage());
        }
    }

    // =========================
    // UPDATE ARTIST
    // =========================
    public void updateArtist(String name, int id) {

        if (id <= 0) {
            System.out.println("Invalid artist ID.");
            return;
        }

        if (name == null || name.trim().isEmpty()) {
            System.out.println("Artist name is required.");
            return;
        }

        String query = "UPDATE artists SET name = ? WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, name);
            prep.setInt(2, id);

            int rows = prep.executeUpdate();

            System.out.println(
                    rows > 0
                            ? "Artist " + name + " updated successfully."
                            : "Failed to update artist."
            );

            System.out.println();

            readAllArtists();

        } catch (SQLException e) {
            System.out.println("Update Artist: " + e.getMessage());
        }
    }

    // =========================
    // ARCHIVE ARTIST
    // =========================
    public void archiveArtist(int id) {

        if (id <= 0) {
            System.out.println("Invalid artist ID.");
            return;
        }

        String query = "UPDATE artists SET is_archived = 1 WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);

            int rows = prep.executeUpdate();

            System.out.println(
                    rows > 0
                            ? "Artist " + id + " archived successfully.\n"
                            : "Failed to archive artist."
            );

            readAllArtists();

        } catch (SQLException e) {
            System.err.println("Archive Artist: " + e.getMessage());
        }
    }

    // =========================
    // RESTORE ARTIST
    // =========================
    public void restoreArtist(int id) {

        if (id <= 0) {
            System.out.println("Invalid artist ID.");
            return;
        }

        String query = "UPDATE artists SET is_archived = 0 WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);

            int rows = prep.executeUpdate();

            System.out.println(
                    rows > 0
                            ? "Artist " + id + " restored successfully.\n"
                            : "Failed to restore artist."
            );

            readAllArtists();

        } catch (SQLException e) {
            System.err.println("Restore Artist: " + e.getMessage());
        }
    }

    // =========================
    // DELETE ARTIST
    // =========================
    public void deleteArtist(int id) {

        if (id <= 0) {
            System.out.println("Invalid artist ID.");
            return;
        }

        String query = "DELETE FROM artists WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);

            int rows = prep.executeUpdate();

            System.out.println(
                    rows > 0
                            ? "Artist " + id + " deleted successfully.\n"
                            : "Failed to delete artist."
            );

            readAllArtists();

        } catch (SQLException e) {
            System.err.println("Delete Artist: " + e.getMessage());
        }
    }

    // =========================
    // READ ARTIST BY ID
    // =========================
    public void readArtistById(int id) {

        String query = "SELECT * FROM artists WHERE id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, id);

            ResultSet res = prep.executeQuery();

            System.out.println("+-------+----------------------+");
            System.out.printf("| %-5s | %-20s |%n", "ID", "Name");
            System.out.println("+-------+----------------------+");

            if (res.next()) {
                System.out.printf(
                        "| %-5d | %-20s |%n",
                        res.getInt("id"),
                        res.getString("name")
                );
            } else {
                System.out.println("| Artist not found.      |");
            }

            System.out.println("+-------+----------------------+");

        } catch (SQLException e) {
            System.out.println("Read Artist By Id: " + e.getMessage());
        }
    }

    // =========================
    // SEARCH ARTIST
    // =========================
    public void searchArtist(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Search keyword cannot be empty.");
            return;
        }

        String query = "SELECT * FROM artists WHERE name LIKE ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, "%" + keyword.trim() + "%");

            ResultSet res = prep.executeQuery();

            System.out.println("+-------+----------------------+");
            System.out.printf("| %-5s | %-20s |%n", "ID", "Name");
            System.out.println("+-------+----------------------+");

            boolean found = false;

            while (res.next()) {
                found = true;

                System.out.printf(
                        "| %-5d | %-20s |%n",
                        res.getInt("id"),
                        res.getString("name")
                );
            }

            if (!found) {
                System.out.println("| No artists found.      |");
            }

            System.out.println("+-------+----------------------+");

        } catch (SQLException e) {
            System.out.println("Search Artist: " + e.getMessage());
        }
    }

    // =========================
    // READ ARCHIVED ARTISTS
    // =========================
    public void readAllArchivedArtists() {

        String query = "SELECT * FROM artists WHERE is_archived = 1";

        try (Connection conn = dbConnection.connect();
             Statement stmnt = conn.createStatement();
             ResultSet result = stmnt.executeQuery(query)) {

            System.out.println("+-------+----------------------+");
            System.out.printf("| %-5s | %-20s |%n", "ID", "Name");
            System.out.println("+-------+----------------------+");

            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");

                System.out.printf(
                        "| %-5d | %-20s |%n",
                        id,
                        name
                );
            }

            System.out.println("+-------+----------------------+");

        } catch (SQLException e) {
            System.err.println("Get All Archived Artists: " + e.getMessage());
        }
    }
}