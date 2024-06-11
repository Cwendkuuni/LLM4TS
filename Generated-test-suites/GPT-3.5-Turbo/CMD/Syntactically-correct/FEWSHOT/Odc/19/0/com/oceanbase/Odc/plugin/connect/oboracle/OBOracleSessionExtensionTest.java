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
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();

        sessionExtension.killQuery(connection, "123");

        verify(connection, times(1)).prepareStatement("KILL QUERY 123");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();

        when(sessionExtension.getCurrentSchema(connection)).thenReturn("schema1");

        sessionExtension.switchSchema(connection, "schema2");

        verify(connection, times(1)).prepareStatement("ALTER SESSION SET CURRENT_SCHEMA='schema2'");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn("schema1");

        String schema = sessionExtension.getCurrentSchema(connection);

        assertNotNull(schema);
        assertEquals("schema1", schema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn("123");

        String connectionId = sessionExtension.getConnectionId(connection);

        assertNotNull(connectionId);
        assertEquals("123", connectionId);
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();

        when(oceanBaseConnection.getServerThreadId()).thenReturn(456);

        String connectionId = sessionExtension.getConnectionId(oceanBaseConnection);

        assertNotNull(connectionId);
        assertEquals("456", connectionId);
    }
}