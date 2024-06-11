package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.SQLException;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private Connection connection;

    @Mock
    private OceanBaseConnection oceanBaseConnection;

    private OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();

    @Test
    public void testKillQuery() {
        sessionExtension.killQuery(connection,"123");
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY 123");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(sessionExtension.getCurrentSchema(connection)).thenReturn("schemaName");
        sessionExtension.switchSchema(connection, "schemaName");
        verify(JdbcOperationsUtil.getJdbcOperations(connection), never()).update(anyString());
        verify(sessionExtension.getCurrentSchema(connection)).equals("schemaName");
    }

    @Test
    public void testGetCurrentSchema() {
        sessionExtension.getCurrentSchema(connection);
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class);
    }

    @Test
    public void testGetConnectionId() {
        when(connection instanceof OceanBaseConnection).thenReturn(false);
        sessionExtension.getConnectionId(connection);
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).queryForObject("select userenv('sessionid') from dual", String.class);
    }

    @Test
    public void testGetConnectionIdOceanBaseConnection() {
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        String serverThreadId = "123";
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(Long.parseLong(serverThreadId));
        assertEquals(serverThreadId, sessionExtension.getConnectionId(connection));
        verify(JdbcOperationsUtil.getJdbcOperations(connection), times(1)).queryForObject("select userenv('sessionid') from dual", String.class);
    }
}