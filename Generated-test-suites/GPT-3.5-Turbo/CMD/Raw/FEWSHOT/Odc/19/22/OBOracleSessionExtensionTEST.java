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
    public void testKillQuery() throws Exception {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        
        sessionExtension.killQuery(connection, "12345");
        
        verify(connection, times(1)).prepareStatement("KILL QUERY 12345");
        verify(connection, times(1)).execute("KILL QUERY 12345");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        
        when(sessionExtension.getCurrentSchema(connection)).thenReturn("schema1");
        
        sessionExtension.switchSchema(connection, "schema2");
        
        verify(connection, times(1)).prepareStatement("ALTER SESSION SET CURRENT_SCHEMA='schema2'");
        verify(connection, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA='schema2'");
    }
    
    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn("schema1");
        
        assertEquals("schema1", sessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(OceanBaseConnection.class);
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn("12345");
        assertEquals("12345", sessionExtension.getConnectionId(connection));

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class)).thenThrow(new SQLException());
        
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(5678L);
        assertEquals("5678", sessionExtension.getConnectionId(connection));
    }
}