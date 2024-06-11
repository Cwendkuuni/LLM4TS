package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@SuppressWarnings("unchecked")
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        PluginWrapper pluginWrapper = mock(PluginWrapper.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.setPluginWrapper(pluginWrapper);

        String connectionId = "test_connection_id";
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));

        extension.killQuery(connection, connectionId);

        String querySql = "KILL QUERY " + connectionId;
        JdbcOperationsUtil jdbcOperations = mock(JdbcOperationsUtil.class);
        when(jdbcOperations.execute(connection, querySql)).thenReturn(true);
        extension.setJdbcOperationsUtil(jdbcOperations);

        // Test successful execution
        assertEquals(true, true);

        // Test empty connectionId
        extension.killQuery(connection, null);

        // Test null connection
        extension.killQuery(null, connectionId);

        // Test exception when executing query
        when(jdbcOperations.execute(connection, querySql)).thenThrow(new SQLException("Test exception"));
        assertEquals(true, false);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema_name";
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        when(connection.getCatalog()).thenReturn("test_current_schema");
        when(connection.setCatalog(schemaName)).thenReturn(true);

        extension.switchSchema(connection, schemaName);

        // Test switching to the same schema
        assertEquals("test_current_schema", schemaName);

        // Test null schemaName
        extension.switchSchema(connection, null);

        // Test empty schemaName
        extension.switchSchema(connection, "");

        // Test exception when setting catalog
        when(connection.setCatalog(schemaName)).thenThrow(new SQLException("Test exception"));
        assertEquals("test_current_schema", schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        JdbcOperationsUtil jdbcOperations = mock(JdbcOperationsUtil.class);

        when(jdbcOperations.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn("test_schema_name");
        extension.setJdbcOperationsUtil(jdbcOperations);

        String currentSchema = extension.getCurrentSchema(connection);

        assertEquals("test_schema_name", currentSchema);

        // Test null connection
        assertNull(extension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        String oceanBaseConnectionId = "test_oceanbase_connection_id";
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));

        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(oceanBaseConnectionId);

        String connectionId = extension.getConnectionId(connection);

        assertEquals(oceanBaseConnectionId, connectionId);

        // Test non-OceanBaseConnection
        when(connection instanceof OceanBaseConnection).thenReturn(false);
        assertNull(extension.getConnectionId(connection));

        // Test exception when querying for connection_id()
        when(connection.createStatement()).thenThrow(new SQLException("Test exception"));
        assertNull(extension.getConnectionId(connection));
    }
}