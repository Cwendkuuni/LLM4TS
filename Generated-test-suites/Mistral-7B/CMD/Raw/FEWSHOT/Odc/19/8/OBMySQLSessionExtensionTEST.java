package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

public class OBMySQLSessionExtensionTest {

    private JdbcOperationsUtil jdbcOperationsUtilMock;
    private Connection connectionMock;

    @Before
    public void setup() {
        jdbcOperationsUtilMock = Mockito.mock(JdbcOperationsUtil.class);
        connectionMock = Mockito.mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.setJdbcOperationsUtil(jdbcOperationsUtilMock);

        String connectionId = "testConnectionId";
        when(jdbcOperationsUtilMock.execute(eq(connectionMock), anyString())).thenReturn(null);
        obMySQLSessionExtension.killQuery(connectionMock, connectionId);
        verify(jdbcOperationsUtilMock, times(1)).execute(eq(connectionMock), eq("KILL QUERY " + connectionId));
    }

    @Test(expected = SQLException.class)
    public void testKillQuery_Exception() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.setJdbcOperationsUtil(jdbcOperationsUtilMock);

        String connectionId = "testConnectionId";
        when(jdbcOperationsUtilMock.execute(eq(connectionMock), anyString())).thenThrow(new SQLException());
        obMySQLSessionExtension.killQuery(connectionMock, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String schemaName = "testSchemaName";
        Connection connection = connectionMock;
        String currentSchema = "testCurrentSchema";

        when(jdbcOperationsUtilMock.queryForObject(eq(connectionMock), anyString())).thenReturn(currentSchema);
        when(connection.setCatalog(anyString())).thenReturn(connection);

        obMySQLSessionExtension.switchSchema(connection, schemaName);
        verify(connection, times(1)).setCatalog(schemaName);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_Exception() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String schemaName = "testSchemaName";
        Connection connection = connectionMock;
        String currentSchema = "testCurrentSchema";

        when(jdbcOperationsUtilMock.queryForObject(eq(connectionMock), anyString())).thenThrow(new SQLException());
        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testgetCurrentSchema() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String expectedSchema = "testSchemaName";
        Connection connection = connectionMock;

        when(jdbcOperationsUtilMock.queryForObject(eq(connectionMock), anyString())).thenReturn(expectedSchema);

        assertEquals(expectedSchema, obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testgetConnectionId() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String connectionId = "testConnectionId";
        Connection connection = connectionMock;

        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(connectionId.toCharArray());

        assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testgetConnectionId_NullConnection() {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        Connection connection = null;

        assertNull(obMySQLSessionExtension.getConnectionId(connection));
    }
}