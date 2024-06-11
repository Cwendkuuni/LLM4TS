package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Test;
import org.junit.Assert;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    @Test(expected = NullPointerException.class)
    public void testKillQueryNullConnection() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.killQuery(null, "123");
    }

    @Test(expected = NullPointerException.class)
    public void testSwitchSchemaNullConnection() throws SQLException {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.switchSchema(null, "schema");
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaSQLException() throws SQLException {
        Connection conn = mock(Connection.class);
        when(conn.createStatement()).thenThrow(new SQLException());

        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.switchSchema(conn, "schema");
    }

    @Test(expected = NullPointerException.class)
    public void testGetCurrentSchemaNullConnection() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        Assert.assertNull(extension.getCurrentSchema(null));
    }

    @Test(expected = NullPointerException.class)
    public void testGetConnectionIdNullConnection() {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        Assert.assertNull(extension.getConnectionId(null));
    }

    @Test
    public void testGetConnectionIdWithNonOceanBaseConnection() {
        Connection conn = mock(Connection.class);

        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        Assert.assertNull(extension.getConnectionId(conn));
    }
}
