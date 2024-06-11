package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection mockConnection;
    private String mockSchemaName = "testSchema";
    private String mockConnectionId = "12345";

    @Before
    public void setup() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        mockConnection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        obOracleSessionExtension.killQuery(mockConnection, mockConnectionId);
        verify(mockConnection, times(1)).createStatement();
        verify(mockConnection.createStatement(), times(1)).executeUpdate("KILL QUERY " + mockConnectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(obOracleSessionExtension.getCurrentSchema(mockConnection)).thenReturn("defaultSchema");
        
        obOracleSessionExtension.switchSchema(mockConnection, mockSchemaName);
        verify(obOracleSessionExtension, times(1)).getCurrentSchema(mockConnection);
        
        obOracleSessionExtension.switchSchema(mockConnection, "defaultSchema");
        verify(obOracleSessionExtension, times(2)).getCurrentSchema(mockConnection);
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "schema";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(anyString(), eq(String.class))).thenReturn(expectedSchema);

        String result = obOracleSessionExtension.getCurrentSchema(mockConnection);
        assertNotNull(result);
        assertEquals(expectedSchema, result);
    }

    @Test
    public void testGetConnectionId() {
        String expectedConnectionId = "54321";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(anyString(), eq(String.class))).thenReturn(expectedConnectionId);

        String result = obOracleSessionExtension.getConnectionId(mockConnection);
        assertNotNull(result);
        assertEquals(expectedConnectionId, result);

        OceanBaseConnection mockOceanBaseConnection = mock(OceanBaseConnection.class);
        when(mockConnection instanceof OceanBaseConnection).thenReturn(true);
        when(mockOceanBaseConnection.getServerThreadId()).thenReturn(54321L);
        assertEquals("54321", obOracleSessionExtension.getConnectionId(mockOceanBaseConnection));
    }
}