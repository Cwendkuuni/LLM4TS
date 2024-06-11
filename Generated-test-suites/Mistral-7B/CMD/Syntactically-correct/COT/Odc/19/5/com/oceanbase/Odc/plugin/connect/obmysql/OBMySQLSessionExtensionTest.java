package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.impl.DefaultExtensionManager;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

/**
 * Test class for OBMySQLSessionExtension
 */
public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private SessionExtensionPoint sessionExtensionPoint;

    @Before
    public void setup() {
        DefaultExtensionManager extensionManager = new DefaultExtensionManager();
        sessionExtensionPoint = (SessionExtensionPoint) extensionManager.getExtensionByType(OBMySQLSessionExtension.class);
        connection = getMockConnection();
    }

    // Test killQuery method

    @Test
    public void testKillQuery() {
        String connectionId = "1234567890";
        sessionExtensionPoint.killQuery(connection, connectionId);
    }

    @Test(expected = SQLException.class)
    public void testKillQueryNullConnection() {
        String connectionId = "1234567890";
        sessionExtensionPoint.killQuery(null, connectionId);
    }

    @Test(expected = NullPointerException.class)
    public void testKillQueryNullConnectionId() {
        sessionExtensionPoint.killQuery(connection, null);
    }

    @Test(expected = NullPointerException.class)
    public void testKillQueryEmptyConnectionId() {
        sessionExtensionPoint.killQuery(connection, "");
    }

    // Test switchSchema method

    @Test
    public void testSwitchSchemaValidSchema() throws SQLException {
        String currentSchema = "testSchema";
        String newSchema = "newSchema";
        String expectedNewSchema = "newSchema";
        sessionExtensionPoint.switchSchema(connection, currentSchema);
        sessionExtensionPoint.switchSchema(connection, newSchema);
        String actualNewSchema = sessionExtensionPoint.getCurrentSchema(connection);
        assertEquals(expectedNewSchema, actualNewSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaInvalidSchema() throws SQLException {
        String currentSchema = "testSchema";
        String newSchema = null;
        sessionExtensionPoint.switchSchema(connection, currentSchema);
        sessionExtensionPoint.switchSchema(connection, newSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaSchemaNotFound() throws SQLException {
        String currentSchema = "testSchema";
        String newSchema = "notExistSchema";
        sessionExtensionPoint.switchSchema(connection, currentSchema);
        sessionExtensionPoint.switchSchema(connection, newSchema);
    }

    @Test(expected = NullPointerException.class)
    public void testSwitchSchemaNullConnection() {
        String schemaName = "newSchema";
        sessionExtensionPoint.switchSchema(null, schemaName);
    }

    @Test(expected = NullPointerException.class)
    public void testSwitchSchemaNullSchemaName() {
        sessionExtensionPoint.switchSchema(connection, null);
    }

    // Test getCurrentSchema method

    @Test
    public void testGetCurrentSchemaValidSchema() throws SQLException {
        String expectedSchema = "testSchema";
        String actualSchema = sessionExtensionPoint.getCurrentSchema(connection);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test(expected = SQLException.class)
    public void testGetCurrentSchemaNullConnection() {
        sessionExtensionPoint.getCurrentSchema(null);
    }

    // Test getConnectionId method

    @Test
    public void testGetConnectionIdValidConnection() {
        String expectedConnectionId = "1234567890";
        String actualConnectionId = sessionExtensionPoint.getConnectionId(connection);
        assertEquals(expectedConnectionId, actualConnectionId);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionIdNullConnection() {
        sessionExtensionPoint.getConnectionId(null);
    }

    @Test
    public void testGetConnectionIdOceanBaseConnection() {
        String expectedConnectionId = "1234567890";
        sessionExtensionPoint.getConnectionId(new OceanBaseConnection());
        assertEquals(expectedConnectionId, sessionExtensionPoint.getConnectionId(connection));
    }

    private Connection getMockConnection() {
        // Initialize a mock connection
        return null;
    }
}