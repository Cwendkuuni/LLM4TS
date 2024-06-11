package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;
import org.pf4j.impl.DefaultExtensionPoint;

import com.oceanbase.jdbc.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@SuppressWarnings("unchecked")
public class OBOracleSessionExtensionTest {

    private ExtensionManager extensionManager;
    private PluginWrapper<OBOracleSessionExtension> pluginWrapper;
    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        extensionManager = mock(ExtensionManager.class);
        pluginWrapper = mock(PluginWrapper.class);
        when(pluginWrapper.getImplementations(SessionExtensionPoint.class)).thenReturn(new DefaultExtensionPoint[]{obOracleSessionExtension = new OBOracleSessionExtension()});
        when(extensionManager.getExtension(SessionExtensionPoint.class)).thenReturn(pluginWrapper);
        connection = mock(Connection.class);
    }

    @After
    public void tearDown() throws Exception {
        if (obOracleSessionExtension != null) {
            obOracleSessionExtension = null;
        }
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "123456";
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        obOracleSessionExtension.killQuery(connection, connectionId);
        // Add more assertions or verification code here if needed
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        String currentSchema = "old_schema";
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(StringUtils.quoteOracleIdentifier(String.format("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")))).thenReturn(currentSchema);
        when(JdbcOperationsUtil.getJdbcOperations(connection).update(StringUtils.quoteOracleIdentifier(String.format("ALTER SESSION SET CURRENT_SCHEMA=%s", schemaName)))).thenReturn(1);

        obOracleSessionExtension.switchSchema(connection, schemaName);
        assertEquals(schemaName, getCurrentSchema(connection));

        // Add more assertions or verification code here if needed
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = "test_schema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(StringUtils.quoteOracleIdentifier(String.format("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")))).thenReturn(currentSchema);
        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));

        // Add more test cases or assertions here if needed
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "123456";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(String.format("select userenv('sessionid') from dual"), String.class)).thenReturn(connectionId);
        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));

        when(connection instanceof OceanBaseConnection).thenReturn(true);
        String serverThreadId = "789101112";
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(serverThreadId);
        assertEquals(serverThreadId, obOracleSessionExtension.getConnectionId(connection));

        // Add more test cases or assertions here if needed
    }
}