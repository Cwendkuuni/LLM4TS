package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

        // Test for successful execution
        obOracleSessionExtension.killQuery(connection, "123456");
        verify(connection, times(1)).prepareStatement("KILL QUERY 123456");

        // Test for SQLException
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException());
        obOracleSessionExtension.killQuery(connection, "789101");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

        // Test for switch to same schema
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("schema1");
        obOracleSessionExtension.switchSchema(connection, "schema1");
        verify(connection, never()).prepareStatement(anyString());

        // Test for successful switch
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("schema2");
        obOracleSessionExtension.switchSchema(connection, "schema3");
        verify(connection, times(1)).prepareStatement("ALTER SESSION SET CURRENT_SCHEMA=\"schema3\"");

        // Test for SQLException
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException());
        obOracleSessionExtension.switchSchema(connection, "schema4");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

        // Test for successful execution
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn("schema1");
        assertEquals("schema1", obOracleSessionExtension.getCurrentSchema(connection));

        // Test for SQLException
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenThrow(new SQLException());
        assertNull(obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

        // Test for successful execution
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn("123456");
        assertEquals("123456", obOracleSessionExtension.getConnectionId(connection));

        // Test for OceanBaseConnection
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenThrow(new SQLException());
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(654321L);
        assertEquals("654321", obOracleSessionExtension.getConnectionId(oceanBaseConnection));

        // Test for SQLException
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenThrow(new SQLException());
        assertNull(obOracleSessionExtension.getConnectionId(connection));
    }
}