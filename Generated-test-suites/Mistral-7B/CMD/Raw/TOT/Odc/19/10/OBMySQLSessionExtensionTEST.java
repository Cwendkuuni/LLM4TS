package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionManager;
import org.pf4j.impl.DefaultExtensionManager;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

public class OBMySQLSessionExtensionTest {

    private SessionExtensionPoint obMySQLSessionExtension;
    private Connection connection;

    @Before
    public void setup() throws Exception {
        ExtensionManager extensionManager = new DefaultExtensionManager();
        obMySQLSessionExtension = (OBMySQLSessionExtension) extensionManager.getExtension("OBMySQLSessionExtension");
        connection = obMySQLSessionExtension.getConnection("jdbc:oceansbase:localhost:10000/testDB");
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        obMySQLSessionExtension.killQuery(connection, connectionId);
        fail("Expected exception not thrown");
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithInvalidConnectionId() throws SQLException {
        String connectionId = "invalid_connection_id";
        obMySQLSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        obMySQLSessionExtension.switchSchema(connection, "testSchema");
        assertEquals("testSchema", obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithInvalidSchemaName() throws SQLException {
        obMySQLSessionExtension.switchSchema(connection, "invalid_schema_name");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertNotNull(currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertNotNull(connectionId);
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        String connectionId = obMySQLSessionExtension.getConnectionId(new OceanBaseConnection(connection.createStatement().getConnection()));
        assertNotNull(connectionId);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionIdWithInvalidConnection() {
        obMySQLSessionExtension.getConnectionId(null);
    }

    @After
    public void teardown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}