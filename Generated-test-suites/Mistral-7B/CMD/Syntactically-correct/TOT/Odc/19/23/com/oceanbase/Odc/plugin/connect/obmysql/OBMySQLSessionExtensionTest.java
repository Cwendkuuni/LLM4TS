package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.impl.DefaultExtensionPoint;
import org.pf4j.impl.DefaultExtensionPointRegistry;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.JdbcOperationsUtil;

@SuppressWarnings("deprecation")
public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private DefaultExtensionPointRegistry extensionRegistry;
    private ExtensionManager extensionManager;
    private SessionExtensionPoint sessionExtensionPoint;

    @Before
    public void setup() throws SQLException, Exception {
        extensionRegistry = new DefaultExtensionPointRegistry();
        extensionManager = new ExtensionManager(extensionRegistry);
        connection = JdbcOperationsUtil.getConnection();
        extensionManager.scanAndLoadExtensions("com.oceanbase.odc.plugin.connect.obmysql");
        sessionExtensionPoint = (SessionExtensionPoint) extensionManager.getExtensionByType(SessionExtensionPoint.class);
    }

    @After
    public void teardown() throws SQLException {
        JdbcOperationsUtil.closeConnection(connection);
        extensionManager.shutdown();
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = sessionExtensionPoint.getConnectionId(connection);
        String schemaName = "test_schema";
        String query = "SELECT * FROM test_table";

        // Normal test case
        sessionExtensionPoint.killQuery(connection, connectionId);
        sessionExtensionPoint.switchSchema(connection, schemaName);
        sessionExtensionPoint.executeQuery(connection, query);
        assertEquals("No results", JdbcOperationsUtil.getJdbcOperations(connection).queryForList(query).size());

        // Edge case: Kill query of non-existent connection
        sessionExtensionPoint.killQuery(connection, "non_existent_connection_id");

        // Error scenario: Kill query while not connected
        JdbcOperationsUtil.closeConnection(connection);
        sessionExtensionPoint.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String connectionId = sessionExtensionPoint.getConnectionId(connection);
        String currentSchema = sessionExtensionPoint.getCurrentSchema(connection);
        String newSchema = "test_schema";

        // Normal test case
        sessionExtensionPoint.switchSchema(connection, newSchema);
        assertEquals(newSchema, sessionExtensionPoint.getCurrentSchema(connection));

        // Edge case: Switch schema to the same schema
        sessionExtensionPoint.switchSchema(connection, currentSchema);

        // Error scenario: Switch schema while not connected
        JdbcOperationsUtil.closeConnection(connection);
        sessionExtensionPoint.switchSchema(connection, newSchema);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String connectionId = sessionExtensionPoint.getConnectionId(connection);
        String expectedSchema = "test_schema";

        // Normal test case
        sessionExtensionPoint.switchSchema(connection, expectedSchema);
        assertEquals(expectedSchema, sessionExtensionPoint.getCurrentSchema(connection));

        // Edge case: Get current schema of non-existent connection
        sessionExtensionPoint.killQuery(connection, connectionId);
        assertNull(sessionExtensionPoint.getCurrentSchema(connection));

        // Error scenario: Get current schema while not connected
        JdbcOperationsUtil.closeConnection(connection);
        assertNull(sessionExtensionPoint.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = sessionExtensionPoint.getConnectionId(connection);
        OceanBaseConnection obConnection = (OceanBaseConnection) connection;

        // Normal test case
        assertEquals(obConnection.getServerThreadId(), connectionId);

        // Edge case: Get connection id of non-OceanBaseConnection
        sessionExtensionPoint.killQuery(connection, connectionId);
        assertNull(sessionExtensionPoint.getConnectionId(connection));

        // Error scenario: Get connection id while not connected
        JdbcOperationsUtil.closeConnection(connection);
        assertNull(sessionExtensionPoint.getConnectionId(connection));
    }
}