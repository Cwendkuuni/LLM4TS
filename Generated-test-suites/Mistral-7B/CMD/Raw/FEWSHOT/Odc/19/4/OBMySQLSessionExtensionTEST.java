package com.oceanbase.odc.plugin.connect.obmysql;

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
import org.pf4j.ExtensionManager;
import org.pf4j.ExtensionPoint;
import org.pf4j.impl.DefaultExtensionPoint;
import org.pf4j.impl.DefaultExtensionRegistry;
import org.pf4j.impl.DefaultExtensionRegistry.ExtensionDescriptor;
import org.pf4j.impl.DefaultExtensionRegistry.ExtensionRegistryImpl;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.JdbcOperationsUtil;

@SuppressWarnings("unchecked")
public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private ExtensionRegistryImpl extensionRegistry;
    private ExtensionManager extensionManager;
    private SessionExtensionPoint sessionExtensionPoint;

    @Before
    public void setup() {
        connection = mock(Connection.class);
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(connection.unwrap(JdbcOperations.class)).thenReturn(jdbcOperations);

        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);

        extensionRegistry = new ExtensionRegistryImpl();
        DefaultExtensionPoint sessionExtensionPointMock = new DefaultExtensionPoint(SessionExtensionPoint.class);
        sessionExtensionPoint = new OBMySQLSessionExtension();
        ExtensionDescriptor extensionDescriptor = new ExtensionDescriptor("com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension", sessionExtensionPoint);
        extensionRegistry.addExtension(extensionDescriptor);

        extensionManager = new ExtensionManager(extensionRegistry);
        extensionManager.init();
    }

    @Test
    public void testKillQuery() throws SQLException {
        OBMySQLSessionExtension sessionExtension = (OBMySQLSessionExtension) extensionManager.getExtensions(SessionExtensionPoint.class).get(0);
        String connectionId = "connection_id_123";
        when(jdbcOperations.execute(sessionExtension.killQuery(connection, connectionId))).thenReturn(null);

        sessionExtension.killQuery(connection, connectionId);
        assertEquals(null, sessionExtension.killQuery(connection, connectionId));
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() throws SQLException {
        OBMySQLSessionExtension sessionExtension = (OBMySQLSessionExtension) extensionManager.getExtensions(SessionExtensionPoint.class).get(0);
        String connectionId = "connection_id_123";
        when(jdbcOperations.execute(sessionExtension.killQuery(connection, connectionId))).thenThrow(new SQLException("Test exception"));

        sessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        OBMySQLSessionExtension sessionExtension = (OBMySQLSessionExtension) extensionManager.getExtensions(SessionExtensionPoint.class).get(0);
        String schemaName = "test_schema";
        String currentSchema = "current_schema";

        when(jdbcOperations.queryForObject(sessionExtension.getCurrentSchema(connection), String.class)).thenReturn(currentSchema);
        when(jdbcOperations.execute(sessionExtension.switchSchema(connection, schemaName))).thenReturn(null);

        sessionExtension.switchSchema(connection, schemaName);
        assertEquals(null, sessionExtension.switchSchema(connection, schemaName));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        OBMySQLSessionExtension sessionExtension = (OBMySQLSessionExtension) extensionManager.getExtensions(SessionExtensionPoint.class).get(0);
        String schemaName = "test_schema";
        String currentSchema = "current_schema";

        when(jdbcOperations.queryForObject(sessionExtension.getCurrentSchema(connection), String.class)).thenReturn(currentSchema);
        when(jdbcOperations.execute(sessionExtension.switchSchema(connection, schemaName))).thenThrow(new SQLException("Test exception"));

        sessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        OBMySQLSessionExtension sessionExtension = (OBMySQLSessionExtension) extensionManager.getExtensions(SessionExtensionPoint.class).get(0);
        String currentSchema = "test_schema";

        when(jdbcOperations.queryForObject(sessionExtension.getCurrentSchema(connection), String.class)).thenReturn(currentSchema);

        assertEquals(currentSchema, sessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        OBMySQLSessionExtension sessionExtension = (OBMySQLSessionExtension) extensionManager.getExtensions(SessionExtensionPoint.class).get(0);
        String connectionId = "connection_id_123";

        when(jdbcOperations.queryForObject(sessionExtension.getConnectionId(connection), String.class)).thenReturn(Optional.ofNullable(connectionId));

        assertEquals(connectionId, sessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdNull() throws SQLException {
        OBMySQLSessionExtension sessionExtension = (OBMySQLSessionExtension) extensionManager.getExtensions(SessionExtensionPoint.class).get(0);

        when(jdbcOperations.queryForObject(sessionExtension.getConnectionId(connection), String.class)).thenReturn(Optional.empty());

        assertNull(sessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdOceanBaseConnection() throws SQLException {
        OBMySQLSessionExtension sessionExtension = (OBMySQLSessionExtension) extensionManager.getExtensions(SessionExtensionPoint.class).get(0);
        String connectionId = "server_thread_id_123";

        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(Long.parseLong(connectionId));

        assertEquals(connectionId, sessionExtension.getConnectionId(connection));
    }
}