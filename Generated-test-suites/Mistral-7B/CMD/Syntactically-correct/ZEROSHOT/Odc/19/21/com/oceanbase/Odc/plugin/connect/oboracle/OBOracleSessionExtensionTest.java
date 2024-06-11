package com.oceanbase.odc.plugin.connect.oboracle.test;

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
import org.pf4j.TestExtension;
import org.pf4j.TestExtensionManager;
import org.pf4j.util.PropertiesUtil;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@SuppressWarnings("unused")
public class OBOracleSessionExtensionTest {

    private SessionExtensionPoint obOracleSessionExtension;
    private JdbcOperationsUtil jdbcOperationsUtil;
    private ExtensionManager extensionManager;
    private Connection connection;

    @Before
    public void setup() throws Exception {
        jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        extensionManager = new TestExtensionManager();
        obOracleSessionExtension = new OBOracleSessionExtension() {
            {
                setJdbcOperationsUtil(jdbcOperationsUtil);
                setExtensionManager(extensionManager);
            }
        };
        extensionManager.addExtension(obOracleSessionExtension);
        connection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        when(jdbcOperationsUtil.execute(Objects.any(Connection.class), "KILL QUERY " + anyString())).thenReturn(1);
        obOracleSessionExtension.killQuery(connection, "connectionId");
        verify(jdbcOperationsUtil).execute(connection, "KILL QUERY connectionId");
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() throws SQLException {
        when(jdbcOperationsUtil.execute(Objects.any(Connection.class), "KILL QUERY " + anyString()))
                .thenThrow(new SQLException("error message"));
        obOracleSessionExtension.killQuery(connection, "connectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        String currentSchema = "oldSchema";
        when(jdbcOperationsUtil.update(Objects.any(Connection.class), anyString())).thenReturn(1);
        when(jdbcOperationsUtil.queryForObject(Objects.any(Connection.class), anyString(), String.class))
                .thenReturn(currentSchema);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        verify(jdbcOperationsUtil).update(connection, "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
        assertEquals(schemaName, getCurrentSchema());
    }

    @Test
    public void testSwitchSchemaWithSameSchema() throws SQLException {
        String schemaName = "testSchema";
        when(jdbcOperationsUtil.queryForObject(Objects.any(Connection.class), anyString(), String.class))
                .thenReturn(schemaName);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        verify(jdbcOperationsUtil, never()).update(connection, "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        String schemaName = "testSchema";
        when(jdbcOperationsUtil.update(Objects.any(Connection.class), anyString())).thenThrow(new SQLException("error message"));
        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String currentSchema = "testSchema";
        when(jdbcOperationsUtil.queryForObject(Objects.any(Connection.class), anyString(), String.class))
                .thenReturn(currentSchema);
        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchemaWithNullResult() throws SQLException {
        when(jdbcOperationsUtil.queryForObject(Objects.any(Connection.class), anyString(), String.class))
                .thenReturn(null);
        assertNull(obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        String connectionId = "testConnectionId";
        when(connection instanceof OceanBaseConnection)
                .thenReturn(new OceanBaseConnection() {
                    @Override
                    public String getServerThreadId() {
                        return connectionId;
                    }
                });
        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithNullConnectionId() {
        when(connection instanceof OceanBaseConnection).thenReturn(null);
        assertEquals(Optional.empty(), obOracleSessionExtension.getConnectionId(connection));
    }

    private String getCurrentSchema() {
        return PropertiesUtil.getProperty("currentSchema", "defaultSchema");
    }
}