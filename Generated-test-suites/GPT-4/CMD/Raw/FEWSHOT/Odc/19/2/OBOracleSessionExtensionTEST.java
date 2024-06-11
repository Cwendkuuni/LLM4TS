package com.oceanbase.odc.plugin.connect.oboracle;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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
        String connectionId = "12345";
        sessionExtension.killQuery(mockConnection, connectionId);
        verify(mockConnection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema_unmatchedSchema() throws SQLException {
        String schemaName = "new_schema";
        when(sessionExtension.getCurrentSchema(mockConnection)).thenReturn("old_schema");
        sessionExtension.switchSchema(mockConnection, schemaName);
        verify(mockConnection, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }

    @Test
    public void testSwitchSchema_matchedSchema() throws SQLException {
        String schemaName = "same_schema";
        when(sessionExtension.getCurrentSchema(mockConnection)).thenReturn(schemaName);
        sessionExtension.switchSchema(mockConnection, schemaName);
        verify(mockConnection, times(0)).update(anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        sessionExtension.getCurrentSchema(mockConnection);
        verify(mockConnection, times(1)).queryForObject(querySql, String.class);
    }

    @Test
    public void testGetConnectionId() {
        String querySql = "select userenv('sessionid') from dual";
        sessionExtension.getConnectionId(mockConnection);
        verify(mockConnection, times(1)).queryForObject(querySql, String.class);
    }
}