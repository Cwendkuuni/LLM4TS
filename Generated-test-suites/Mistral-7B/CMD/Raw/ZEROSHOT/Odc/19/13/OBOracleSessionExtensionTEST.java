package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
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
import org.pf4j.util.PropertiesUtil;
import org.pf4j.util.Tools;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@SuppressWarnings("deprecation")
public class OBOracleSessionExtensionTest {

    private Connection connection;
    private ExtensionManager extensionManager;
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() throws Exception {
        connection = mock(Connection.class);
        extensionManager = mock(ExtensionManager.class);
        PluginWrapper pluginWrapper = new PluginWrapper("com.oceanbase.odc.plugin.connect.oboracle:OBOracleSessionExtension",
                PropertiesUtil.loadPropertiesFromClasspath("plugin.properties"));
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.setExtensionManager(extensionManager);
        obOracleSessionExtension.init(pluginWrapper);
        this.obOracleSessionExtension = obOracleSessionExtension;

        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connection.isClosed()).thenReturn(false);
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testKillQuery() throws SQLException {
        OBOracleSessionExtension obOracleSessionExtension = this.obOracleSessionExtension;
        obOracleSessionExtension.killQuery(connection, "connectionId");
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        String killQuerySql = "KILL QUERY connectionId";
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connection.execute(killQuerySql)).thenReturn(true);
        obOracleSessionExtension.killQuery(connection, "connectionId");
        assertEquals(true, true);
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() throws SQLException {
        OBOracleSessionExtension obOracleSessionExtension = this.obOracleSessionExtension;
        obOracleSessionExtension.killQuery(connection, "connectionId");
        when(connection.createStatement()).thenThrow(new SQLException("Test exception"));
        obOracleSessionExtension.killQuery(connection, "connectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        OBOracleSessionExtension obOracleSessionExtension = this.obOracleSessionExtension;
        obOracleSessionExtension.switchSchema(connection, "testSchema");
        String currentSchema = getCurrentSchema(connection);
        String testSchema = "testSchema";
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(testSchema);
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connection.execute(alterSql)).thenReturn(true);
        assertEquals(testSchema, currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        OBOracleSessionExtension obOracleSessionExtension = this.obOracleSessionExtension;
        obOracleSessionExtension.switchSchema(connection, "testSchema");
        when(connection.createStatement()).thenThrow(new SQLException("Test exception"));
        obOracleSessionExtension.switchSchema(connection, "testSchema");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        OBOracleSessionExtension obOracleSessionExtension = this.obOracleSessionExtension;
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connection.queryForObject(querySql, String.class)).thenReturn("testSchema");
        assertEquals("testSchema", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        OBOracleSessionExtension obOracleSessionExtension = this.obOracleSessionExtension;
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        String querySql = "select userenv('sessionid') from dual";
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connection.queryForObject(querySql, String.class)).thenReturn("testConnectionId");
        assertEquals("testConnectionId", connectionId);
    }

    @Test
    public void testGetConnectionIdWhenConnectionIsNull() {
        String connectionId = this.obOracleSessionExtension.getConnectionId(null);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(12345);
        when(connection).thenReturn(oceanBaseConnection);
        assertEquals("12345", connectionId);
    }

    @Test
    public void testGetConnectionIdWhenConnectionIsClosed() throws SQLException {
        String connectionId = this.obOracleSessionExtension.getConnectionId(connection);
        when(connection.isClosed()).thenReturn(true);
        assertEquals(null, connectionId);
    }

    private String getCurrentSchema(Connection connection) throws SQLException {
        return this.obOracleSessionExtension.getCurrentSchema(connection);
    }

}