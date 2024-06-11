package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.Connection;
import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {
    @Mock
    private Connection mockConnection;

    private OBOracleSessionExtension sessionExtension;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        sessionExtension = new OBOracleSessionExtension();
    }
  
    @Test
    public void testKillQuery() {
        String connectionId = "testConnectionId";
        sessionExtension.killQuery(mockConnection, connectionId);
        verify(mockConnection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        when(mockConnection.getCurrentSchema()).thenReturn(schemaName);
        sessionExtension.switchSchema(mockConnection, schemaName);
        verify(mockConnection, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
    }

    @Test
    public void testGetCurrentSchema() {
        String schemaQuery = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        when(mockConnection.getJdbcOperations().queryForObject(schemaQuery, String.class)).thenReturn("testSchema");
        assertEquals("testSchema", sessionExtension.getCurrentSchema(mockConnection));
    }

    @Test
    public void testGetConnectionId() {
        String connectionIdQuery = "select userenv('sessionid') from dual";
        when(mockConnection.getJdbcOperations().queryForObject(connectionIdQuery, String.class)).thenReturn("testConnectionId");
        assertEquals("testConnectionId", sessionExtension.getConnectionId(mockConnection));
    }
}