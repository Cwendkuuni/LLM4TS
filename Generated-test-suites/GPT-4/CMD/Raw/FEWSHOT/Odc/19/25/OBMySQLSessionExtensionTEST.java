package com.oceanbase.odc.plugin.connect.obmysql;

import java.sql.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connection;
    @Mock
    private OceanBaseConnection oceanBaseConnection;
    @Mock
    private JdbcOperationsUtil jdbcOperationsUtil;

    private OBMySQLSessionExtension obMySQLSessionExtension;
    private String schemaName;

    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        schemaName = "testSchema";
    }

    @Test
    public void testKillQuery() {
        String connectionId = "1001";
        obMySQLSessionExtension.killQuery(connection, connectionId);
        verify(jdbcOperationsUtil).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(jdbcOperationsUtil.getCurrentSchema(connection)).thenReturn("testSchema");
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        verify(connection, never()).setCatalog(schemaName);
        verify(jdbcOperationsUtil).getCurrentSchema(connection);

        when(jdbcOperationsUtil.getCurrentSchema(connection)).thenReturn("otherSchema");
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        verify(connection).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT DATABASE()";
        when(jdbcOperationsUtil.queryForObject(querySql, String.class)).thenReturn(schemaName);
        String result = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, result);
    }

    @Test
    public void testGetConnectionId() {
        String querySql = "select connection_id()";
        when(jdbcOperationsUtil.queryForObject(querySql, String.class)).thenReturn("1001");
        String result = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals("1001", result);

        when(jdbcOperationsUtil.queryForObject(querySql, String.class)).thenThrow(new RuntimeException());
        when(connection instanceof OceanBaseConnection).thenReturn(oceanBaseConnection);
        String oceanBaseConnectionId = "1001";
        when(oceanBaseConnection.getServerThreadId()).thenReturn(Long.parseLong(oceanBaseConnectionId));
        result = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals(oceanBaseConnectionId, result);
    }
}