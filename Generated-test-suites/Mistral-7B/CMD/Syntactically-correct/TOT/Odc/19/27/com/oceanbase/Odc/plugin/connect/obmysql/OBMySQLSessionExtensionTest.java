package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionManager;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

public class OBMySQLSessionExtensionTest {

    private static SessionExtensionPoint extension;
    private Connection connection;

    @Before
    public void setup() throws Exception {
        ExtensionManager extensionManager = new ExtensionManager();
        extension = (OBMySQLSessionExtension) extensionManager.getExtensionByType(SessionExtensionPoint.class);
        connection = extension.getConnection();
    }

    @Test
    public void testKillQuery() {
        String connectionId = extension.getConnectionId(connection);
        extension.killQuery(connection, connectionId);
        assertThrows(SQLException.class, () -> extension.getConnectionId(connection));
    }

    @Test
    public void testKillQueryInvalidConnectionId() {
        String invalidConnectionId = "12345";
        assertThrows(SQLException.class, () -> extension.killQuery(connection, invalidConnectionId));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        extension.switchSchema(connection, schemaName);
        assertEquals(schemaName, extension.getCurrentSchema(connection));
    }

    @Test
    public void testSwitchSchemaInvalidSchemaName() throws SQLException {
        String invalidSchemaName = "";
        assertThrows(SQLException.class, () -> extension.switchSchema(connection, invalidSchemaName));
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = extension.getCurrentSchema(connection);
        String schemaName = "testSchema";
        extension.switchSchema(connection, schemaName);
        assertEquals(schemaName, extension.getCurrentSchema(connection));
        extension.switchSchema(connection, currentSchema);
        assertEquals(currentSchema, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = extension.getConnectionId(connection);
        assertEquals(connectionId, extension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdInvalidConnection() {
        Connection invalidConnection = null;
        assertNull(extension.getConnectionId(invalidConnection));
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}