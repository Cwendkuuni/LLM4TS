package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBMySQLSessionExtensionTest {

    OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "123456";
        obMySQLSessionExtension.killQuery(connection, connectionId);
        // Add assertions here if needed
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        // Add assertions here if needed
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        String expectedSchema = "test_schema";
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(jdbcOperationsUtil.queryForObject("SELECT DATABASE()", String.class)).thenReturn(expectedSchema);
        obMySQLSessionExtension.getCurrentSchema(connection);
        // Add assertions here if needed
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        String expectedConnectionId = "123456";
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(123456L);
        when(jdbcOperationsUtil.queryForObject("select connection_id()", String.class)).thenReturn(expectedConnectionId);
        obMySQLSessionExtension.getConnectionId(connection);
        // Add assertions here if needed
    }

    // Add more test cases for edge cases and exceptions here

}