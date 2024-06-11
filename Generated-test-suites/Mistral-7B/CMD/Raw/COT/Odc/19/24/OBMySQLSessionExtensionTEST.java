package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@SuppressWarnings("unchecked")
public class OBMySQLSessionExtensionTest {

    private SessionExtensionPoint obMySQLSessionExtension;
    private PluginWrapper pluginWrapper;
    private Connection connectionMock;

    @Before
    public void setUp() {
        pluginWrapper = mock(PluginWrapper.class);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.init(pluginWrapper);
        connectionMock = mock(Connection.class);
    }

    @Test
    public void testKillQuery_ValidConnectionId() throws SQLException {
        when(JdbcOperationsUtil.getJdbcOperations(connectionMock)).thenReturn(mock(JdbcOperationsUtil.class));

        obMySQLSessionExtension.killQuery(connectionMock, "connectionId");

        // You can add assertions here to check the behavior of the method
    }

    @Test(expected = SQLException.class)
    public void testKillQuery_InvalidConnectionId() throws SQLException {
        when(JdbcOperationsUtil.getJdbcOperations(connectionMock)).thenThrow(new SQLException());

        obMySQLSessionExtension.killQuery(connectionMock, "invalidConnectionId");
    }

    @Test
    public void testSwitchSchema_ValidSchemaName() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";

        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn(currentSchema -> currentSchema);
        when(connection.setCatalog(anyString())).thenReturn(connection);

        obMySQLSessionExtension.switchSchema(connection, schemaName);

        // You can add assertions here to check the behavior of the method
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_InvalidSchemaName() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        String currentSchema = "existingSchema";

        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn(currentSchema);
        when(connection.setCatalog(anyString())).thenThrow(new SQLException());

        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema_ValidConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        String currentSchema = "testSchema";

        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn(currentSchema);

        String result = obMySQLSessionExtension.getCurrentSchema(connection);

        assertEquals(currentSchema, result);
    }

    @Test
    public void testGetCurrentSchema_InvalidConnection() {
        Connection connection = mock(Connection.class);

        String result = obMySQLSessionExtension.getCurrentSchema(connection);

        assertNull(result);
    }

    @Test
    public void testGetConnectionId_ValidConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "testId";

        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenThrow(new SQLException());
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(connectionId);

        String result = obMySQLSessionExtension.getConnectionId(connection);

        assertEquals(connectionId, result);
    }

    @Test
    public void testGetConnectionId_InvalidConnection() {
        Connection connection = mock(Connection.class);

        String result = obMySQLSessionExtension.getConnectionId(connection);

        assertNull(result);
    }
}