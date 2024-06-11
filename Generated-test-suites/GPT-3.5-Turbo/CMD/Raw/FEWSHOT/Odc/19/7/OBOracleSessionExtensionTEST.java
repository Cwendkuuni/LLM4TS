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
    public void testKillQuery() {
        Connection mockConnection = mock(Connection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        
        obOracleSessionExtension.killQuery(mockConnection, "12345");
        
        verify(mockConnection, times(1)).prepareStatement("KILL QUERY 12345");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        
        doReturn("schema1").when(obOracleSessionExtension).getCurrentSchema(mockConnection);
        
        obOracleSessionExtension.switchSchema(mockConnection, "schema1");
        
        verify(mockConnection, never()).prepareStatement("ALTER SESSION SET CURRENT_SCHEMA='schema1'");
        
        obOracleSessionExtension.switchSchema(mockConnection, "schema2");
        
        verify(mockConnection, times(1)).prepareStatement("ALTER SESSION SET CURRENT_SCHEMA='schema2'");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection mockConnection = mock(Connection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        
        when(obOracleSessionExtension.getCurrentSchema(mockConnection)).thenReturn("schema");
        
        String currentSchema = obOracleSessionExtension.getCurrentSchema(mockConnection);
        
        assertNotNull(currentSchema);
        assertEquals("schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection mockConnection = mock(Connection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        
        try {
            obOracleSessionExtension.getConnectionId(mockConnection);
        } catch(SQLException e) {
            // exception handling
        }
        
        verify(mockConnection, times(1)).prepareStatement("select userenv('sessionid') from dual");
        
        when(mockConnection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) mockConnection).getServerThreadId()).thenReturn(123);
        
        String connectionId = obOracleSessionExtension.getConnectionId(mockConnection);
        
        assertNotNull(connectionId);
        assertEquals("123", connectionId);
    }
}