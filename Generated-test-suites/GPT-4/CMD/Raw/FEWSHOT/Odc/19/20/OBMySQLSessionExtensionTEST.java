
package com.oceanbase.odc.plugin.connect.obmysql;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.*;
import org.mockito.stubbing.OngoingStubbing;

public class OBMySQLSessionExtensionTest {
    @Mock
    private OceanBaseConnection mockConnection;

    private OBMySQLSessionExtension sessionExtension;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "123";
        sessionExtension.killQuery(mockConnection, connectionId);
        verify(mockConnection).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test-schema";
        when(mockConnection.getCatalog()).thenReturn(schemaName);
        sessionExtension.switchSchema(mockConnection, schemaName);
        verify(mockConnection, never()).setCatalog(anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "test-schema";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("SELECT DATABASE()", String.class)).thenReturn(expectedSchema);
        String actualSchema = sessionExtension.getCurrentSchema(mockConnection);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        String expectedConnectionId = "1";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select connection_id()", String.class)).thenReturn(expectedConnectionId);
        String actualConnectionId = sessionExtension.getConnectionId(mockConnection);
        assertEquals(expectedConnectionId, actualConnectionId);
    }

    @Test
    public void testGetConnectionId_error() {
        String expectedConnectionId = "1";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException());
        when(mockConnection.getServerThreadId()).thenReturn(Long.valueOf(expectedConnectionId));
        String actualConnectionId = sessionExtension.getConnectionId(mockConnection);
        assertEquals(expectedConnectionId, actualConnectionId);
    }
}
