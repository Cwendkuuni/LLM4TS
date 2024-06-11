package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

@SuppressWarnings("deprecation")
public class OBOracleSessionExtensionTest {

    @Mock
    private Connection connection;

    @Mock
    private ExtensionManager extensionManager;

    @Mock
    private PluginWrapper pluginWrapper;

    @InjectMocks
    private OBOracleSessionExtension obOracleSessionExtension;

    private String currentSchema;
    private String schemaName;
    private String connectionId;

    @Before
    public void setUp() throws Exception {
        when(extensionManager.getExtension("com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension"))
                .thenReturn(pluginWrapper);
        when(pluginWrapper.getImplementedProfiles()).thenReturn(new String[]{"SessionExtensionPoint"});
        when(pluginWrapper.getInstance()).thenReturn(obOracleSessionExtension);

        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(obOracleSessionExtension.getJdbcOperationsUtil()).thenReturn(jdbcOperationsUtil);

        currentSchema = "testSchema";
        schemaName = "testSchema2";
        connectionId = "testConnectionId";
    }

    @After
    public void tearDown() throws Exception {
        // Reset mocks for next test
        reset(connection, extensionManager, pluginWrapper, obOracleSessionExtension, jdbcOperationsUtil);
    }

    @Test
    public void testKillQuery() throws SQLException {
        when(connection.isClosed()).thenReturn(false);
        obOracleSessionExtension.killQuery(connection, connectionId);
        verify(jdbcOperationsUtil).getJdbcOperations(connection).execute("KILL QUERY " + connectionId);
    }

    @Test(expected = SQLException.class)
    public void testKillQueryConnectionClosed() throws SQLException {
        when(connection.isClosed()).thenReturn(true);
        obOracleSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(connection.isClosed()).thenReturn(false);
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));

        obOracleSessionExtension.switchSchema(connection, schemaName);
        verify(jdbcOperationsUtil).getJdbcOperations(connection).update("ALTER SESSION SET CURRENT_SCHEMA="
                + StringUtils.quoteOracleIdentifier(schemaName));

        verify(connection).createStatement();

        String newSchema = "testSchema3";
        obOracleSessionExtension.switchSchema(connection, newSchema);
        verify(jdbcOperationsUtil).getJdbcOperations(connection).update("ALTER SESSION SET CURRENT_SCHEMA="
                + StringUtils.quoteOracleIdentifier(newSchema));

        verify(connection, times(2)).createStatement();
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaConnectionClosed() throws SQLException {
        when(connection.isClosed()).thenReturn(true);
        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        when(connection.isClosed()).thenReturn(false);
        when(jdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class))
                .thenReturn(currentSchema);

        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testGetCurrentSchemaConnectionClosed() throws SQLException {
        when(connection.isClosed()).thenReturn(true);
        obOracleSessionExtension.getCurrentSchema(connection);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(connection.isClosed()).thenReturn(false);
        when(connection instanceof OceanBaseConnection).thenReturn(true);

        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));

        when(connection instanceof OceanBaseConnection).thenReturn(false);
        String serverThreadId = "testServerThreadId";
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(jdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class))
                .thenReturn(serverThreadId);

        assertEquals(serverThreadId, obOracleSessionExtension.getConnectionId(connection));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionIdConnectionClosed() throws SQLException {
        when(connection.isClosed()).thenReturn(true);
        obOracleSessionExtension.getConnectionId(connection);
    }
}