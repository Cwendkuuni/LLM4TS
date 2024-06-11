package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection mockConnection;
    
    @Mock
    private OceanBaseConnection obConnection;
    
    private OBMySQLSessionExtension sessionExtension;

    @Before
    public void setUp() throws SQLException {
        sessionExtension = new OBMySQLSessionExtension();
        when(obConnection.getServerThreadId()).thenReturn(1L);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String currentSchema = "test";
        when(mockConnection.getCatalog()).thenReturn(currentSchema);

        sessionExtension.switchSchema(mockConnection, currentSchema);

        verify(mockConnection, never()).setCatalog(anyString());
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaFailure() throws SQLException {
        doThrow(new SQLException()).when(mockConnection).setCatalog(anyString());
        
        sessionExtension.switchSchema(mockConnection, "new_schema");
    }

    @Test
    public void testKillQuery() throws SQLException {
        String testConnId = "123";
        sessionExtension.killQuery(mockConnection, testConnId);
        
        verify(mockConnection, times(1)).createStatement();
        verify(mockConnection.createStatement(), times(1)).execute("KILL QUERY " + testConnId);
    }

    @Test
    public void testGetCurrentSchema() {
        String expected = "test_schema";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(any(String.class), eq(String.class)))
                .thenReturn(expected);
        
        String result = sessionExtension.getCurrentSchema(mockConnection);
        assertEquals(expected, result);
    }

    @Test
    public void testGetConnectionId() {
        String expected = "test_id";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(any(String.class), eq(String.class)))
                .thenReturn(expected);

        String result = sessionExtension.getConnectionId(mockConnection);
        assertEquals(expected, result);
    }

    @Test
    public void testGetConnectionIdException() {
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(any(String.class), eq(String.class)))
                .thenThrow(new RuntimeException());
        
        String result = sessionExtension.getConnectionId(obConnection);
        assertEquals("1", result);
    }
}
