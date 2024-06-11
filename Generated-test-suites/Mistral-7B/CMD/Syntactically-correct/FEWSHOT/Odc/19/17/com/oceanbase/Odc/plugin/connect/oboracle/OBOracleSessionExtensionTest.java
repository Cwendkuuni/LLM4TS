package com.oceanbase.odc.plugin.connect.oboracle;

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

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@SuppressWarnings("unchecked")
public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() throws Exception {
        Connection mockConnection = mock(Connection.class);
        ExtensionManager extensionManager = mock(ExtensionManager.class);
        PluginWrapper pluginWrapper = mock(PluginWrapper.class);
        when(pluginWrapper.getExtension(SessionExtensionPoint.class)).thenReturn(new OBOracleSessionExtension());
        when(extensionManager.getExtension(SessionExtensionPoint.class)).thenReturn(pluginWrapper);

        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        sessionExtension.setExtensionManager(extensionManager);

        String connectionId = "connection_id";
        when(mockConnection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(mockConnection.execute(anyString())).thenReturn(true);

        sessionExtension.killQuery(mockConnection, connectionId);
        assertEquals("KILL QUERY " + connectionId, JdbcOperationsUtil.getJdbcOperations(mockConnection).executeQuery(anyString()).toString());
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        ExtensionManager extensionManager = mock(ExtensionManager.class);
        PluginWrapper pluginWrapper = mock(PluginWrapper.class);
        when(pluginWrapper.getExtension(SessionExtensionPoint.class)).thenReturn(new OBOracleSessionExtension());
        when(extensionManager.getExtension(SessionExtensionPoint.class)).thenReturn(pluginWrapper);

        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        sessionExtension.setExtensionManager(extensionManager);

        String newSchemaName = "testSchema";
        String currentSchema = "currentSchema";

        when(mockConnection.createStatement()).thenReturn(mock(java.sql.Statement.class));

        sessionExtension.switchSchema(mockConnection, newSchemaName);
        StringUtils stringUtils = new StringUtils();
        String expectedAlterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + stringUtils.quoteOracleIdentifier(newSchemaName);
        assertEquals(expectedAlterSql, JdbcOperationsUtil.getJdbcOperations(mockConnection).update(anyString()).toString());
    }

    @Test
    public void testGetCurrentSchema() throws Exception {
        Connection mockConnection = mock(Connection.class);
        ExtensionManager extensionManager = mock(ExtensionManager.class);
        PluginWrapper pluginWrapper = mock(PluginWrapper.class);
        when(pluginWrapper.getExtension(SessionExtensionPoint.class)).thenReturn(new OBOracleSessionExtension());
        when(extensionManager.getExtension(SessionExtensionPoint.class)).thenReturn(pluginWrapper);

        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        sessionExtension.setExtensionManager(extensionManager);

        String currentSchema = "testSchema";
        when(mockConnection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(mockConnection.queryForObject(anyString(), anyString())).thenReturn(currentSchema);

        assertEquals(currentSchema, sessionExtension.getCurrentSchema(mockConnection));
    }

    @Test
    public void testGetConnectionId() throws Exception {
        Connection mockConnection = mock(Connection.class);
        ExtensionManager extensionManager = mock(ExtensionManager.class);
        PluginWrapper pluginWrapper = mock(PluginWrapper.class);
        when(pluginWrapper.getExtension(SessionExtensionPoint.class)).thenReturn(new OBOracleSessionExtension());
        when(extensionManager.getExtension(SessionExtensionPoint.class)).thenReturn(pluginWrapper);

        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        sessionExtension.setExtensionManager(extensionManager);

        String connectionId = "connection_id";
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(mockConnection.isWrapperFor(OceanBaseConnection.class)).thenReturn(true);
        when(mockConnection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(Long.parseLong(connectionId));

        assertEquals(connectionId, sessionExtension.getConnectionId(mockConnection));

        assertNull(sessionExtension.getConnectionId(null));
    }
}