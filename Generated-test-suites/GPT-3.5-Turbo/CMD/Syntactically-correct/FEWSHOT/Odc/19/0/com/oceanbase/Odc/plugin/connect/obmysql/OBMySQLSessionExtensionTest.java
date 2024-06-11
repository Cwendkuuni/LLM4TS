package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension sessionExtension;
    private Connection mockConnection;

    @Before
    public void setUp() {
        sessionExtension = new OBMySQLSessionExtension();
        mockConnection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "123456";
        sessionExtension.killQuery(mockConnection, connectionId);
        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection), times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        when(sessionExtension.getCurrentSchema(mockConnection)).thenReturn("default_schema");
        sessionExtension.switchSchema(mockConnection, schemaName);
        verify(mockConnection, times(1)).setCatalog(schemaName);

        sessionExtension.switchSchema(mockConnection, "default_schema");
        verifyNoMoreInteractions(mockConnection);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String expectedSchema = "test_schema";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn(expectedSchema);
        assertEquals(expectedSchema, sessionExtension.getCurrentSchema(mockConnection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String serverThreadId = "654321";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select connection_id()", String.class))
                .thenReturn(null);
        when(mockConnection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) mockConnection).getServerThreadId()).thenReturn(Long.parseLong(serverThreadId));

        assertEquals(serverThreadId, sessionExtension.getConnectionId(mockConnection));
    }
}