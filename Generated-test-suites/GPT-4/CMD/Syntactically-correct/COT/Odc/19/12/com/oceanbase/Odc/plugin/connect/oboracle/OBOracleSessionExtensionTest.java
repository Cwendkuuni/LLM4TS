package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension sessionExtension;
    private Connection mockConnection;

    @Before
    public void setup() {
        sessionExtension = new OBOracleSessionExtension();
        mockConnection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "connectionId";
        sessionExtension.killQuery(mockConnection, connectionId);
        verify(mockConnection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() {
        String schemaName = "schemaName";
        sessionExtension.switchSchema(mockConnection, schemaName);
        verify(mockConnection, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedQuery = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        sessionExtension.getCurrentSchema(mockConnection);
        verify(mockConnection, times(1)).queryForObject(expectedQuery, String.class);
    }

    @Test
    public void testGetConnectionId() {
        String expectedQuery = "select userenv('sessionid') from dual";
        sessionExtension.getConnectionId(mockConnection);
        verify(mockConnection, times(1)).queryForObject(expectedQuery, String.class);
    }

    // Testing edge case when connection is an instance of OceanBaseConnection
    @Test
    public void testOceanBaseGetConnectionId() {
        OceanBaseConnection mockOBConnection = mock(OceanBaseConnection.class);
        when(mockOBConnection.getServerThreadId()).thenReturn(123);

        sessionExtension.getConnectionId(mockOBConnection);
        verify(mockOBConnection, times(1)).getServerThreadId();
    }
    
    // Test case for switchSchema when current schema is equal to the provided schema
    @Test
    public void testSwitchSchema_NoOp() {
        String schemaName = "schemaName";
        when(mockConnection.queryForObject(any,String.class)).thenReturn(schemaName);

        sessionExtension.switchSchema(mockConnection, schemaName);
        verify(mockConnection, never()).update(any(String.class));
    }
}

