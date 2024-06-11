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
import org.pf4j.PluginWrapper;
import org.pf4j.util.PropertiesLoader;

import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.ConnectionWrapper;

@SuppressWarnings("unchecked")
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        ConnectionWrapper connectionWrapper = mock(ConnectionWrapper.class);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        when(connectionWrapper.getConnection()).thenReturn(connection);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(1L);

        SessionExtensionPoint sessionExtension = new OBMySQLSessionExtension();
        ExtensionManager extensionManager = mock(ExtensionManager.class);
        PluginWrapper pluginWrapper = mock(PluginWrapper.class);
        PropertiesLoader propertiesLoader = mock(PropertiesLoader.class);
        when(extensionManager.getExtensionByInterface(SessionExtensionPoint.class)).thenReturn(sessionExtension);
        when(extensionManager.getExtension(pluginWrapper)).thenReturn(sessionExtension);
        when(pluginWrapper.getProperties()).thenReturn(propertiesLoader);

        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);

        sessionExtension.killQuery(connectionWrapper, "connectionId");
        assertEquals("KILL QUERY connectionId", JdbcOperationsUtil.getJdbcOperations(connection).queryCall(true, "exec", "KILL QUERY connectionId"));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        String schemaName = "testSchema";

        SessionExtensionPoint sessionExtension = new OBMySQLSessionExtension();

        when(oceanBaseConnection.getCatalog()).thenReturn("defaultSchema");

        sessionExtension.switchSchema(connection, schemaName);

        assertEquals(schemaName, oceanBaseConnection.getCatalog());
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        when(oceanBaseConnection.getCatalog()).thenReturn("testSchema");

        SessionExtensionPoint sessionExtension = new OBMySQLSessionExtension();

        assertEquals("testSchema", sessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        String connectionId = "testConnectionId";

        SessionExtensionPoint sessionExtension = new OBMySQLSessionExtension();

        when(connection instanceof OceanBaseConnection).thenReturn(true);

        assertEquals(connectionId, sessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWhenNotOceanBaseConnection() throws SQLException {
        Connection connection = mock(Connection.class);

        SessionExtensionPoint sessionExtension = new OBMySQLSessionExtension();

        assertNull(sessionExtension.getConnectionId(connection));
    }
}