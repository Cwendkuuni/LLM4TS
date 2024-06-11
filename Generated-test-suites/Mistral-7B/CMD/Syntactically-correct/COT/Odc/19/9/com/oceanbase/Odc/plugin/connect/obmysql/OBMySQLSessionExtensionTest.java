import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.impl.DefaultExtensionManager;
import org.pf4j.impl.DefaultExtensionPoint;
import org.pf4j.impl.DefaultExtensionPointManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(DefaultExtensionPointManager.class)
public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension;
    private Connection connection;
    private DefaultExtensionPointManager extensionPointManager;
    private ExtensionManager extensionManager;

    @BeforeEach
    public void init() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testDB", "root", "password");
        extensionPointManager = new DefaultExtensionPointManager();
        extensionManager = new DefaultExtensionManager(extensionPointManager);
        obMySQLSessionExtension = (OBMySQLSessionExtension) extensionManager.getExtensions(SessionExtensionPoint.class).get(0);
    }

    @Test
    public void testKillQuery() throws SQLException {
        obMySQLSessionExtension.killQuery(connection, "testConnectionId");
    }

    @Test
    public void testSwitchSchema_Valid() throws SQLException {
        obMySQLSessionExtension.switchSchema(connection, "testSchema");
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals("testSchema", currentSchema);
    }

    @Test
    public void testSwitchSchema_SameSchema() throws SQLException {
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        obMySQLSessionExtension.switchSchema(connection, currentSchema);
    }

    @Test
    public void testSwitchSchema_InvalidSchema() throws SQLException {
        assertThrows(SQLException.class, () -> obMySQLSessionExtension.switchSchema(connection, "testSchema123"));
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        String queryResult = JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class);
        assertEquals(queryResult, currentSchema);
    }

    @Test
    public void testGetConnectionId_Valid() throws SQLException {
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        Assertions.assertNotNull(connectionId);
    }

    @Test
    public void testGetConnectionId_Invalid() throws SQLException {
        assertThrows(SQLException.class, () -> obMySQLSessionExtension.getConnectionId(null));
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() throws SQLException {
        Statement stmt = connection.createStatement();
        String querySql = "CREATE DATABASE IF NOT EXISTS testDB2";
        stmt.executeUpdate(querySql);
        OceanBaseConnection obConnection = (OceanBaseConnection) DriverManager.getConnection("jdbc:oceansbase:ob://localhost:2880/testDB2", "root", "password");
        String connectionId = obMySQLSessionExtension.getConnectionId(obConnection);
        Assertions.assertNotNull(connectionId);
    }
}