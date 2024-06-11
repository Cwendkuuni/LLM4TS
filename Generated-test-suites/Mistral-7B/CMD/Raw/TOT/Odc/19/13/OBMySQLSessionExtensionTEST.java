package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private OBMySQLSessionExtension sessionExtension;
    private ExtensionManager extensionManager;

    @Before
    public void setup() throws Exception {
        extensionManager = new ExtensionManager();
        PluginWrapper wrapper = new PluginWrapper("com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension");
        extensionManager.loadPlugin(wrapper);
        sessionExtension = (OBMySQLSessionExtension) extensionManager.getExtension(SessionExtensionPoint.class);
        connection = sessionExtension.getConnection();
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = sessionExtension.getConnectionId(connection);
        String schemaName = "test_schema";
        String query = "SELECT * FROM test_table";

        // Kill non-existent query
        sessionExtension.killQuery(connection, "non_existent_query_id");

        // Kill query with valid connection id
        sessionExtension.killQuery(connection, connectionId);

        // Kill query with valid query
        sessionExtension.killQuery(connection, connectionId);
        sessionExtension.killQuery(connection, connectionId);

        // Verify that the query was killed
        assertThrows(SQLException.class, () -> sessionExtension.getJdbcOperations().queryForObject(query, String[].class));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        String newSchema = "test_schema";

        // Switch to the same schema
        sessionExtension.switchSchema(connection, currentSchema);
        assertEquals(currentSchema, sessionExtension.getCurrentSchema(connection));

        // Switch to a non-existent schema
        assertThrows(SQLException.class, () -> sessionExtension.switchSchema(connection, "non_existent_schema"));

        // Switch to a different schema
        sessionExtension.switchSchema(connection, newSchema);
        assertEquals(newSchema, sessionExtension.getCurrentSchema(connection));

        // Switch back to the original schema
        sessionExtension.switchSchema(connection, currentSchema);
        assertEquals(currentSchema, sessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = sessionExtension.getCurrentSchema(connection);

        // Get current schema when no database is set
        assertThrows(SQLException.class, () -> sessionExtension.getCurrentSchema(connection));

        // Get current schema with a valid database
        String newSchema = "test_schema";
        sessionExtension.switchSchema(connection, newSchema);
        assertEquals(newSchema, sessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId1 = sessionExtension.getConnectionId(connection);
        String connectionId2 = sessionExtension.getConnectionId(connection);

        // Get connection id for the same connection
        assertEquals(connectionId1, connectionId2);

        // Get connection id for a different connection
        Connection connection2 = sessionExtension.getConnection();
        String connectionId3 = sessionExtension.getConnectionId(connection2);
        assertNotEqual(connectionId1, connectionId3);
    }

    @After
    public void teardown() throws SQLException {
        sessionExtension.killQuery(connection, connection.getCatalog());
        connection.close();
        extensionManager.unloadPlugin(sessionExtension.getClass().getName());
    }
}