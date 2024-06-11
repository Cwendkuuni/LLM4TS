package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionManager;

import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private ExtensionManager extensionManager;

    @Before
    public void setUp() throws Exception {
        extensionManager = new ExtensionManager();
        extensionManager.start();
        connection = DriverManager.getConnection("jdbc:oceansbase:local:1000/testDB", "root", "password");
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        extensionManager.stop();
    }

    @Test
    public void testKillQueryWithValidConnectionAndConnectionId() throws SQLException {
        OBMySQLSessionExtension sessionExtension = (OBMySQLSessionExtension) extensionManager.getExtension(SessionExtensionPoint.class, "OBMySQLSessionExtension");
        String connectionId = sessionExtension.getConnectionId(connection);
        sessionExtension.killQuery(connection, connectionId);
        // Verify no exception is thrown
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithInvalidConnection() {
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.killQuery(null, "invalid_connection_id");
    }

    @Test
    public void testKillQueryWithEmptyConnectionId() {
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.killQuery(connection, "");
    }

    @Test
    public void testSwitchSchemaWithValidConnectionAndSchemaName() throws SQLException {
        OBMySQLSessionExtension sessionExtension = (OBMySQLSessionExtension) extensionManager.getExtension(SessionExtensionPoint.class, "OBMySQLSessionExtension");
        String schemaName = "testSchema";
        sessionExtension.switchSchema(connection, schemaName);
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithInvalidConnection() {
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.switchSchema(null, "testSchema");
    }

    @Test
    public void testSwitchSchemaWithEmptySchemaName() {
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.switchSchema(connection, "");
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        assertNull(currentSchema);
    }

    @Test
    public void testSwitchSchemaWithExistingSchemaName() throws SQLException {
        OBMySQLSessionExtension sessionExtension = (OBMySQLSessionExtension) extensionManager.getExtension(SessionExtensionPoint.class, "OBMySQLSessionExtension");
        String schemaName = "testSchema";
        sessionExtension.switchSchema(connection, schemaName);
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);

        sessionExtension.switchSchema(connection, "defaultSchema");
        currentSchema = sessionExtension.getCurrentSchema(connection);
        assertNull(currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithInvalidSchemaName() throws SQLException {
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.switchSchema(connection, "nonexistentSchema");
    }

    @Test
    public void testGetCurrentSchemaWithValidConnection() throws SQLException {
        OBMySQLSessionExtension sessionExtension = (OBMySQLSessionExtension) extensionManager.getExtension(SessionExtensionPoint.class, "OBMySQLSessionExtension");
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        assertNull(currentSchema);

        String schemaName = "testSchema";
        sessionExtension.switchSchema(connection, schemaName);
        currentSchema = sessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
    }

    @Test
    public void testGetCurrentSchemaWithNullConnection() {
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        assertNull(sessionExtension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionIdWithValidConnection() throws SQLException {
        OBMySQLSessionExtension sessionExtension = (OBMySQLSessionExtension) extensionManager.getExtension(SessionExtensionPoint.class, "OBMySQLSessionExtension");
        String connectionId = sessionExtension.getConnectionId(connection);
        assertNotNull(connectionId);
    }

    @Test
    public void testGetConnectionIdWithNullConnection() {
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        assertNull(sessionExtension.getConnectionId(null));
    }
}