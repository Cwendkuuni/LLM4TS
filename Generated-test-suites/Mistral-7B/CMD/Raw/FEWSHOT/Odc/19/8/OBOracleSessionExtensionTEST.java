package com.oceanbase.odc.plugin.connect.oboracle;

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
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.pf4j.impl.DefaultExtensionPoint;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@SuppressWarnings("unchecked")
public class OBOracleSessionExtensionTest {

    private PluginManager pluginManager;
    private PluginWrapper<OBOracleSessionExtension> pluginWrapper;
    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        pluginManager = new PluginManager();
        pluginWrapper = new PluginWrapper<>();
        pluginWrapper.setImplementation(new OBOracleSessionExtension());
        pluginManager.addExtension(pluginWrapper);
        obOracleSessionExtension = (OBOracleSessionExtension) pluginManager.getExtensions(SessionExtensionPoint.class).get(0);
        connection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        when(oceanBaseConnection.getJdbcOperations()).thenReturn(mock(JdbcOperationsUtil.class));

        obOracleSessionExtension.killQuery(connection, "connectionId");

        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(oceanBaseConnection.getJdbcOperations()).thenReturn(jdbcOperationsUtil);
        when(jdbcOperationsUtil.execute("KILL QUERY connectionId")).thenReturn(1);

        obOracleSessionExtension.killQuery(connection, "connectionId");

        assertEquals(1, obOracleSessionExtension.getNumberOfQueriesKilled());
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() throws SQLException {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        when(oceanBaseConnection.getJdbcOperations()).thenReturn(mock(JdbcOperationsUtil.class));

        obOracleSessionExtension.killQuery(connection, "connectionId");

        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(oceanBaseConnection.getJdbcOperations()).thenReturn(jdbcOperationsUtil);
        when(jdbcOperationsUtil.execute("KILL QUERY connectionId")).thenThrow(new SQLException("Test Exception"));

        obOracleSessionExtension.killQuery(connection, "connectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        when(oceanBaseConnection.getJdbcOperations()).thenReturn(mock(JdbcOperationsUtil.class));
        when(oceanBaseConnection.getSchema()).thenReturn("currentSchema");

        obOracleSessionExtension.switchSchema(connection, "newSchema");

        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(oceanBaseConnection.getJdbcOperations()).thenReturn(jdbcOperationsUtil);
        when(jdbcOperationsUtil.update("ALTER SESSION SET CURRENT_SCHEMA=newSchema")).thenReturn(1);

        assertEquals("newSchema", obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        when(oceanBaseConnection.getJdbcOperations()).thenReturn(mock(JdbcOperationsUtil.class));
        when(oceanBaseConnection.getSchema()).thenReturn("currentSchema");

        obOracleSessionExtension.switchSchema(connection, "newSchema");

        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(oceanBaseConnection.getJdbcOperations()).thenReturn(jdbcOperationsUtil);
        when(jdbcOperationsUtil.update("ALTER SESSION SET CURRENT_SCHEMA=newSchema")).thenThrow(new SQLException("Test Exception"));
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        when(oceanBaseConnection.getJdbcOperations()).thenReturn(mock(JdbcOperationsUtil.class));
        when(oceanBaseConnection.getSchema()).thenReturn("currentSchema");

        assertEquals("currentSchema", obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchemaNullConnection() {
        assertNull(obOracleSessionExtension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(1234);

        assertEquals("1234", obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdDualQuery() throws SQLException {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        when(oceanBaseConnection.getJdbcOperations()).thenReturn(mock(JdbcOperationsUtil.class));

        String connectionId = obOracleSessionExtension.getConnectionId(connection);

        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(oceanBaseConnection.getJdbcOperations()).thenReturn(jdbcOperationsUtil);
        when(jdbcOperationsUtil.queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn("1234");

        assertEquals("1234", connectionId);
    }

    @Test
    public void testGetConnectionIdException() throws SQLException {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        when(oceanBaseConnection.getJdbcOperations()).thenReturn(mock(JdbcOperationsUtil.class));

        String connectionId = obOracleSessionExtension.getConnectionId(connection);

        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(oceanBaseConnection.getJdbcOperations()).thenReturn(jdbcOperationsUtil);
        when(jdbcOperationsUtil.queryForObject("select userenv('sessionid') from dual", String.class)).thenThrow(new SQLException("Test Exception"));

        assertEquals(Optional.empty(), obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetNumberOfQueriesKilledZero() {
        assertEquals(0, obOracleSessionExtension.getNumberOfQueriesKilled());
    }

    @Test
    public void testGetNumberOfQueriesKilledOne() {
        obOracleSessionExtension.killQuery(connection, "connectionId");

        assertEquals(1, obOracleSessionExtension.getNumberOfQueriesKilled());
    }
}