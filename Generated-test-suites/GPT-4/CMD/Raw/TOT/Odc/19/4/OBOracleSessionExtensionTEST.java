package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension extension;
    private Connection mockConnection;

    @Before
    public void setup() {
        extension = new OBOracleSessionExtension();
        mockConnection = Mockito.mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "testConnectionId";
        extension.killQuery(mockConnection, connectionId);
        verify(mockConnection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        when(extension.getCurrentSchema(mockConnection)).thenReturn(schemaName);
        
        // Test when current schema equals target schema
        extension.switchSchema(mockConnection, schemaName);
        verify(mockConnection, times(0)).update(anyString());

        // Test when current schema does not equal target schema
        when(extension.getCurrentSchema(mockConnection)).thenReturn("anotherSchema");
        extension.switchSchema(mockConnection, schemaName);
        verify(mockConnection, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        extension.getCurrentSchema(mockConnection);
        verify(mockConnection, times(1)).queryForObject(querySql, String.class);
    }
    
    @Test
    public void testGetConnectionId() {
        String querySql = "select userenv('sessionid') from dual";
        
        // Test when query is successful
        when(mockConnection.queryForObject(querySql, String.class)).thenReturn("testConnectionId");
        assertEquals("testConnectionId", extension.getConnectionId(mockConnection));
        
        // Test when query throws an exception
        when(mockConnection.queryForObject(querySql, String.class)).thenThrow(new RuntimeException());
        assertNull(extension.getConnectionId(mockConnection));

        // Test when connection instance is of type OceanBaseConnection
        when(mockConnection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) mockConnection).getServerThreadId()).thenReturn(12345L);
        assertEquals("12345", extension.getConnectionId(mockConnection));
    }
}