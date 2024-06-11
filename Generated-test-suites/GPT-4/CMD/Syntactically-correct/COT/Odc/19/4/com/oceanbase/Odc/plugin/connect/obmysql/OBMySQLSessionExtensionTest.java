package com.oceanbase.odc.plugin.connect.obmysql;

import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.jdbc.OceanBaseConnection;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        // A basic test case where the connectionId is a valid string.
        extension.killQuery(connection, "123");

        // Edge case where the connectionId is null.
        extension.killQuery(connection, null);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        // A basic test case where the schemaName is a valid string.
        extension.switchSchema(connection, "schema_name");

        // Edge case where the schemaName is null.
        extension.switchSchema(connection, null);
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        // A basic test case
        String schema = extension.getCurrentSchema(connection);
        Assert.assertNotNull(schema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        // Test case for OceanBaseConnection instance
        Connection oceanBaseConnection = Mockito.mock(OceanBaseConnection.class);
        String connectionId = extension.getConnectionId(oceanBaseConnection);
        Assert.assertNotNull(connectionId);

        // Test case for non-OceanBaseConnection instance
        connectionId = extension.getConnectionId(connection);
        Assert.assertNotNull(connectionId);
    }
}
