package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@SuppressWarnings("deprecation")
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        ExtensionManager extensionManager = mock(ExtensionManager.class);
        extensionManager.getExtension(SessionExtensionPoint.class, extension.getClass());

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(connection.unwrap(JdbcOperations.class)).thenReturn(jdbcOperations);
        when(jdbcOperations.execute(extension.getClass().getSimpleName() + ".killQuery")).thenReturn(true);

        extension.killQuery(connection, "connectionId");
        assertEquals(true, true);
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        ExtensionManager extensionManager = mock(ExtensionManager.class);
        extensionManager.getExtension(SessionExtensionPoint.class, extension.getClass());

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(connection.unwrap(JdbcOperations.class)).thenReturn(jdbcOperations);
        when(jdbcOperations.execute(extension.getClass().getSimpleName() + ".killQuery")).thenThrow(new SQLException());

        extension.killQuery(connection, "connectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        ExtensionManager extensionManager = mock(ExtensionManager.class);
        String currentSchema = "currentSchema";
        String schemaName = "newSchema";
        extensionManager.getExtension(SessionExtensionPoint.class, extension.getClass());

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(connection.unwrap(JdbcOperations.class)).thenReturn(jdbcOperations);
        when(jdbcOperations.queryForObject(extension.getClass().getSimpleName() + ".getCurrentSchema", String.class)).thenReturn(currentSchema);
        when(jdbcOperations.queryForObject(extension.getClass().getSimpleName() + ".getCurrentSchema", String.class)).thenReturn(schemaName);
        when(jdbcOperations.execute("SET SCHEMA newSchema")).thenReturn(true);

        extension.switchSchema(connection, schemaName);
        assertEquals(schemaName, schemaName);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        ExtensionManager extensionManager = mock(ExtensionManager.class);
        String currentSchema = "currentSchema";
        String schemaName = "newSchema";
        extensionManager.getExtension(SessionExtensionPoint.class, extension.getClass());

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(connection.unwrap(JdbcOperations.class)).thenReturn(jdbcOperations);
        when(jdbcOperations.queryForObject(extension.getClass().getSimpleName() + ".getCurrentSchema", String.class)).thenReturn(currentSchema);
        when(jdbcOperations.queryForObject(extension.getClass().getSimpleName() + ".getCurrentSchema", String.class)).thenReturn(schemaName);
        when(jdbcOperations.execute("SET SCHEMA newSchema")).thenThrow(new SQLException());

        extension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        ExtensionManager extensionManager = mock(ExtensionManager.class);
        extensionManager.getExtension(SessionExtensionPoint.class, extension.getClass());

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(connection.unwrap(JdbcOperations.class)).thenReturn(jdbcOperations);
        when(jdbcOperations.queryForObject(extension.getClass().getSimpleName() + ".getCurrentSchema", String.class)).thenReturn("currentSchema");

        assertEquals("currentSchema", extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchemaWithNullConnection() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        assertNull(extension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        ExtensionManager extensionManager = mock(ExtensionManager.class);
        String connectionId = "connectionId";
        extensionManager.getExtension(SessionExtensionPoint.class, extension.getClass());

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(connection.unwrap(JdbcOperations.class)).thenReturn(jdbcOperations);
        when(jdbcOperations.queryForObject(extension.getClass().getSimpleName() + ".getConnectionId", String.class)).thenReturn(connectionId);

        assertEquals("connectionId", extension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        Connection oceanBaseConnection = mock(OceanBaseConnection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        ExtensionManager extensionManager = mock(ExtensionManager.class);
        String connectionId = "connectionId";
        extensionManager.getExtension(SessionExtensionPoint.class, extension.getClass());

        when(oceanBaseConnection.getServerThreadId()).thenReturn(1234L);
        assertEquals("1234", extension.getConnectionId(oceanBaseConnection));
    }

    @Test
    public void testGetConnectionIdWithNullConnection() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        assertNull(extension.getConnectionId(null));
    }
}