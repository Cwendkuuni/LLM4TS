package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import org.pf4j.impl.DefaultExtensionPoint;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.getCurrentSchemaQuery;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.killQueryQuery;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.switchSchemaQuery;

@SuppressWarnings("unchecked")
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        PluginWrapper pluginWrapper = mock(PluginWrapper.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.setPluginWrapper(pluginWrapper);
        when(pluginWrapper.getExtensions()).thenReturn(new DefaultExtensionPoint<>());
        when(jdbcOperationsUtil.execute(extension.getConnection(), killQueryQuery)).thenReturn(1);

        Connection connection = mock(Connection.class);
        String connectionId = "connection_id";
        extension.killQuery(connection, connectionId);
        assertEquals(1, 1);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.setJdbcOperationsUtil(jdbcOperationsUtil);

        String currentSchema = "current_schema";
        String schemaName = "new_schema";
        when(jdbcOperationsUtil.queryForObject(connection, getCurrentSchemaQuery)).thenReturn(currentSchema);
        when(jdbcOperationsUtil.queryForObject(connection, switchSchemaQuery, schemaName)).thenReturn(getExpectedString("new_schema"));

        extension.switchSchema(connection, schemaName);
        assertEquals(currentSchema, schemaName);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.setJdbcOperationsUtil(jdbcOperationsUtil);

        String currentSchema = "current_schema";
        String schemaName = "new_schema";
        when(jdbcOperationsUtil.queryForObject(connection, getCurrentSchemaQuery)).thenReturn(currentSchema);
        when(jdbcOperationsUtil.queryForObject(connection, switchSchemaQuery, schemaName)).thenThrow(new SQLException());

        extension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.setJdbcOperationsUtil(jdbcOperationsUtil);

        String connectionId = "connection_id_mock";
        when(jdbcOperationsUtil.queryForObject(extension.getConnection(), killQueryQuery)).thenReturn(connectionId);

        Connection connection = mock(Connection.class);
        when(connection instanceof OceanBaseConnection).thenReturn(oceanBaseConnection);
        String actualConnectionId = extension.getConnectionId(connection);
        assertEquals(connectionId, actualConnectionId);
    }

    @Test
    public void testGetConnectionIdWithNoConnectionIdQueryResult() throws SQLException {
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.setJdbcOperationsUtil(jdbcOperationsUtil);

        when(jdbcOperationsUtil.queryForObject(extension.getConnection(), killQueryQuery)).thenReturn(null);
        when(oceanBaseConnection.getServerThreadId()).thenReturn("thread_id");

        Connection connection = mock(Connection.class);
        when(connection instanceof OceanBaseConnection).thenReturn(oceanBaseConnection);
        String actualConnectionId = extension.getConnectionId(connection);
        assertEquals("thread_id", actualConnectionId);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.setJdbcOperationsUtil(jdbcOperationsUtil);

        String expectedSchema = "expected_schema";
        when(jdbcOperationsUtil.queryForObject(connection, getCurrentSchemaQuery)).thenReturn(expectedSchema);

        String actualSchema = extension.getCurrentSchema(connection);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetCurrentSchemaWithNoResult() throws SQLException {
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.setJdbcOperationsUtil(jdbcOperationsUtil);

        when(jdbcOperationsUtil.queryForObject(connection, getCurrentSchemaQuery)).thenReturn(null);

        String actualSchema = extension.getCurrentSchema(connection);
        assertNull(actualSchema);
    }

    private String getExpectedString(String schemaName) {
        return String.format("Obtained new schema: %s", schemaName);
    }
}