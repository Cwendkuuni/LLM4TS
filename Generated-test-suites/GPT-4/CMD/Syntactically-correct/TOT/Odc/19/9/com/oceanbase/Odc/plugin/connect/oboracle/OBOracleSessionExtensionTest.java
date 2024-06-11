package com.oceanbase.odc.plugin.connect.oboracle;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OBOracleSessionExtensionTest {

    @Mock
    private Connection mockConnection;

    private OBOracleSessionExtension sessionExtension;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "1234";
        sessionExtension.killQuery(mockConnection, connectionId);
        verify(mockConnection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "schema";
        when(mockConnection.getCurrentSchema()).thenReturn(schemaName);
        sessionExtension.switchSchema(mockConnection, schemaName);
        verify(mockConnection, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=" 
            + StringUtils.quoteOracleIdentifier(schemaName));
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "schema";
        when(mockConnection.queryForObject(anyString(), eq(String.class))).thenReturn(expectedSchema);
        String resultSchema = sessionExtension.getCurrentSchema(mockConnection);
        assertEquals(expectedSchema, resultSchema);
    }

    @Test
    public void testGetConnectionId() {
        String expectedId = "1234";
        when(mockConnection.queryForObject(anyString(), eq(String.class))).thenReturn(expectedId);
        String connectionId = sessionExtension.getConnectionId(mockConnection);
        assertEquals(expectedId, connectionId);
    }
}