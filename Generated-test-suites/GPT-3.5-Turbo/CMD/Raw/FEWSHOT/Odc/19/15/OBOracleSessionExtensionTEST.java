package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection mockConnection = mock(Connection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.killQuery(mockConnection, "1234");

        verify(mockConnection, times(1)).execute("KILL QUERY 1234");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.getSchema()).thenReturn("schema1");

        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.switchSchema(mockConnection, "schema2");

        verify(mockConnection, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=\"schema2\"");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection mockConnection = mock(Connection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        when(mockConnection.getSchema()).thenReturn("schema");

        assertEquals("schema", obOracleSessionExtension.getCurrentSchema(mockConnection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        
        when(oceanBaseConnection.getServerThreadId()).thenReturn(1234);
        when(mockConnection.queryForObject(anyString(), eq(String.class))).thenReturn("5678");

        obOracleSessionExtension.getConnectionId(oceanBaseConnection);
        obOracleSessionExtension.getConnectionId(mockConnection);

        verify(mockConnection, times(1)).queryForObject("select userenv('sessionid') from dual", String.class);
        verify(oceanBaseConnection, times(1)).getServerThreadId();
    }
}