package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.ArgumentCaptor;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.exception.PluginException;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint.QueryResult;

@SuppressWarnings("unchecked")
public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException, PluginException {
        Connection connectionMock = mock(Connection.class);
        String connectionId = "123";

        SessionExtensionPoint obOracleSessionExtension = new OBOracleSessionExtension();
        ReflectionUtils.makeAccessible(obOracleSessionExtension, "killQuery");

        when(connectionMock.createStatement()).thenReturn(mock(java.sql.Statement.class));

        obOracleSessionExtension.killQuery(connectionMock, connectionId);

        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        ReflectionUtils.invokeMethod(JdbcOperationsUtil.class, "getJdbcOperations", connectionMock);
        ReflectionUtils.invokeMethod(JdbcOperationsUtil.class, "execute", ReflectionUtils.getFieldValue(JdbcOperationsUtil.class, "jdbcOperations"), connectionMock, queryCaptor.capture());

        assertEquals("KILL QUERY " + connectionId, queryCaptor.getValue());
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String schemaName = "testSchema";

        SessionExtensionPoint obOracleSessionExtension = new OBOracleSessionExtension();

        givenCurrentSchemaIsNotSchemaName(connectionMock);
        obOracleSessionExtension.switchSchema(connectionMock, schemaName);

        givenCurrentSchemaIsSchemaName(connectionMock);
        obOracleSessionExtension.switchSchema(connectionMock, schemaName);
    }

    @Test
    public void testSwitchSchema_currentSchemaNull() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String schemaName = "testSchema";

        SessionExtensionPoint obOracleSessionExtension = new OBOracleSessionExtension();

        givenCurrentSchemaIsNull(connectionMock);
        obOracleSessionExtension.switchSchema(connectionMock, schemaName);

        givenCurrentSchemaIsSchemaName(connectionMock);
        obOracleSessionExtension.switchSchema(connectionMock, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String schemaName = "testSchema";

        SessionExtensionPoint obOracleSessionExtension = new OBOracleSessionExtension();

        givenCurrentSchemaIsNotSchemaName(connectionMock);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connectionMock);
        assertEquals("", currentSchema);

        givenCurrentSchemaIsSchemaName(connectionMock);
        currentSchema = obOracleSessionExtension.getCurrentSchema(connectionMock);
        assertEquals(schemaName, currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connectionMock = mock(Connection.class);

        SessionExtensionPoint obOracleSessionExtension = new OBOracleSessionExtension();

        givenConnectionIsOceanBaseConnection(connectionMock);
        String connectionId = obOracleSessionExtension.getConnectionId(connectionMock);
        assertEquals("", connectionId);

        givenConnectionIsNotOceanBaseConnection(connectionMock);
        connectionId = obOracleSessionExtension.getConnectionId(connectionMock);
        assertEquals(null, connectionId);
    }

    @Test
    public void testGetConnectionId_oceanBaseConnection() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String serverThreadId = "1234";

        SessionExtensionPoint obOracleSessionExtension = new OBOracleSessionExtension();

        givenConnectionIsOceanBaseConnection(connectionMock);
        String connectionId = obOracleSessionExtension.getConnectionId(connectionMock);
        assertEquals(serverThreadId, connectionId);
    }

    private void givenCurrentSchemaIsNotSchemaName(Connection connectionMock) throws SQLException {
        when(connectionMock.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connectionMock.query("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")).thenReturn(null);
    }

    private void givenCurrentSchemaIsNull(Connection connectionMock) {
        PluginWrapper pluginWrapperMock = mock(PluginWrapper.class);
        ExtensionManager extensionManagerMock = mock(ExtensionManager.class);
        when(extensionManagerMock.findExtensionByClass(SessionExtensionPoint.class)).thenReturn(pluginWrapperMock);
        when(pluginWrapperMock.getImplementationInstance()).thenReturn(obOracleSessionExtension);
        when(connectionMock.getExtensionLoader()).thenReturn(extensionManagerMock);
    }

    private void givenCurrentSchemaIsSchemaName(Connection connectionMock) throws SQLException {
        when(connectionMock.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connectionMock.query("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")).thenReturn("testSchema");
    }

    private void givenConnectionIsOceanBaseConnection(Connection connectionMock) {
        when(connectionMock.getClass()).thenReturn(OceanBaseConnection.class);
    }

    private void givenConnectionIsNotOceanBaseConnection(Connection connectionMock) {
        when(connectionMock.getClass()).thenReturn(java.sql.Connection.class);
    }
}