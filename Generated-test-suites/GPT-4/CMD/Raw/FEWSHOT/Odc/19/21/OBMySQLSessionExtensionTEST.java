package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

import lombok.extern.slf4j.Slf4j;

@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connection;

    @Mock
    private OceanBaseConnection oceanBaseConnection;

    @Mock
    private JdbcOperationsUtil jdbcOperationsUtil;

    private SessionExtensionPoint sessionExtensionPoint = new OBMySQLSessionExtension();

    @Test
    public void testKillQuery() {
        String connectionId = "1";
        doNothing().when(jdbcOperationsUtil).execute("KILL QUERY " + connectionId);
        sessionExtensionPoint.killQuery(connection, connectionId);
        verify(jdbcOperationsUtil, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        when(sessionExtensionPoint.getCurrentSchema(connection)).thenReturn(schemaName);
        sessionExtensionPoint.switchSchema(connection, schemaName);
        verify(connection, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT DATABASE()";
        when(jdbcOperationsUtil.queryForObject(querySql, String.class)).thenReturn("testSchema");
        assertEquals("testSchema", sessionExtensionPoint.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        String querySql = "select connection_id()";
        when(jdbcOperationsUtil.queryForObject(querySql, String.class)).thenReturn("1");
        assertEquals("1", sessionExtensionPoint.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWhenExceptionThrown() {
        when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenThrow(new RuntimeException());
        when(oceanBaseConnection.getServerThreadId()).thenReturn(1L);
        assertEquals("1", sessionExtensionPoint.getConnectionId(oceanBaseConnection));
    }
}