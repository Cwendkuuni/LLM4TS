package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.extender.factory.ExtensionFactory;
import org.pf4j.impl.DefaultExtensionPointActivator;
import org.pf4j.impl.DefaultExtensionPointManager;
import org.pf4j.impl.DefaultExtensionRegistry;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@SuppressWarnings("unchecked")
public class OBMySQLSessionExtensionTest {

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);

        when(connection.isClosed()).thenReturn(false);
        when(connection.getCatalog()).thenReturn(null);
        when(connection instanceof OceanBaseConnection).thenReturn(oceanBaseConnection);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(1L);
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        when(jdbcOperationsUtil.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn("schema1");
        when(jdbcOperationsUtil.queryForObject(connection, "SELECT DATABASE()", String.class)).thenThrow(new SQLException());
        when(jdbcOperationsUtil.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn("schema2");

        DefaultExtensionRegistry extensionRegistry = new DefaultExtensionRegistry();
        DefaultExtensionPointManager extensionPointManager = new DefaultExtensionPointManager(extensionRegistry);
        DefaultExtensionPointActivator extensionPointActivator = new DefaultExtensionPointActivator();
        ExtensionFactory extensionFactory = new ExtensionFactory();

        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();

        SessionExtensionPoint sessionExtensionPoint = extensionFactory.createExtension(obMySQLSessionExtension, SessionExtensionPoint.class);
        extensionPointManager.registerExtensionPoint(SessionExtensionPoint.class, sessionExtensionPoint);
        extensionPointActivator.activateExtensionPoint(extensionPointManager, SessionExtensionPoint.class);

        SessionExtensionPoint sessionExtensionPointInstance = extensionPointManager.getExtensions(SessionExtensionPoint.class).get(0);
        sessionExtensionPointInstance.switchSchema(connection, "schema1");
        assertEquals("schema1", JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class));

        sessionExtensionPointInstance.switchSchema(connection, "schema1"); //should not change schema since it's the same
        assertEquals("schema1", JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class));

        sessionExtensionPointInstance.switchSchema(connection, "schema2");
        assertEquals("schema2", JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class));

        sessionExtensionPointInstance.switchSchema(connection, null);
        assertNull(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class));
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);

        when(connection.isClosed()).thenReturn(false);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(1L);
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));

        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();

        SessionExtensionPoint sessionExtensionPoint = obMySQLSessionExtension;
        sessionExtensionPoint.killQuery(connection, "1");
        // Assuming that "KILL QUERY 1" is a successful query
        // Add assertions or expected exceptions here based on the actual behavior of "KILL QUERY"
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);

        when(connection.isClosed()).thenReturn(false);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(1L);
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));

        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();

        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals(null, currentSchema);

        when(jdbcOperationsUtil.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn("schema1");
        currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals("schema1", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);

        when(connection.isClosed()).thenReturn(false);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(1L);
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));

        when(connection instanceof OceanBaseConnection).thenReturn(oceanBaseConnection);
        when(connection.getCatalog()).thenReturn(null);

        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();

        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertNull(connectionId);

        connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals("1", connectionId);
    }
}