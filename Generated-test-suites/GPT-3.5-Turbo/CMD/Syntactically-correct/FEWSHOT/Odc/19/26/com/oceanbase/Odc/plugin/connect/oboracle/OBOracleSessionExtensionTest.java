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
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        
        String connectionId = "12345";
        
        obOracleSessionExtension.killQuery(connection, connectionId);
        
        verify(JdbcOperationsUtil.getJdbcOperations(connection), times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        
        String schemaName = "test_schema";
        
        obOracleSessionExtension.switchSchema(connection, schemaName);
        
        verify(JdbcOperationsUtil.getJdbcOperations(connection), times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        
        String currentSchema = "test_schema";
        
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn(currentSchema);
        
        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        
        String sessionId = "12345";
        
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn(sessionId);
        
        assertEquals(sessionId, obOracleSessionExtension.getConnectionId(connection));
        
        // Test for OceanBaseConnection
        Connection oceanBaseConnection = mock(OceanBaseConnection.class);
        int serverThreadId = 67890;
        
        when(((OceanBaseConnection) oceanBaseConnection).getServerThreadId()).thenReturn(serverThreadId);
        
        assertEquals(String.valueOf(serverThreadId), obOracleSessionExtension.getConnectionId(oceanBaseConnection));
        
    }
}