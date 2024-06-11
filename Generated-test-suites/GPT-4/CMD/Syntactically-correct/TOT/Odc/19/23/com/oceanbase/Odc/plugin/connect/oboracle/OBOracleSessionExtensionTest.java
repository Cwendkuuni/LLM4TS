package com.oceanbase.odc.plugin.connect.oboracle;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;

import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection mockConnection;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        mockConnection = Mockito.mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "1001";
        obOracleSessionExtension.killQuery(mockConnection, connectionId);
        verify(mockConnection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "newSchema";
        String currentSchema = "oldSchema";
        
        when(mockConnection.getCurrentSchema()).thenReturn(currentSchema);
        obOracleSessionExtension.switchSchema(mockConnection, schemaName);
        
        verify(mockConnection, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String schemaName = "schemaName";
        when(mockConnection.queryForObject(any(String.class), eq(String.class))).thenReturn(schemaName);
        String result = obOracleSessionExtension.getCurrentSchema(mockConnection);
        assertEquals(schemaName, result);
    }

    @Test
    public void testGetConnectionId_ValidSessionId() throws SQLException {
        String sessionId = "sessionId";
        when(mockConnection.queryForObject(any(String.class), eq(String.class))).thenReturn(sessionId);
        String result = obOracleSessionExtension.getConnectionId(mockConnection);
        assertEquals(sessionId, result);
    }
    
    @Test
    public void testGetConnectionId_ServerThreadIdFallback() throws SQLException {
        String serverThreadId = "serverThreadId";
        when(mockConnection.queryForObject(any(String.class), eq(String.class))).thenThrow(new SQLException());
        when(mockConnection.getServerThreadId()).thenReturn(serverThreadId);
        String result = obOracleSessionExtension.getConnectionId(mockConnection);
        assertEquals(serverThreadId, result);
    }
}