package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;
import org.powermock.api.mockito.annotation.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.CurrentSchema;

@RunWith(PowerMockRunner.class)
@PrepareForTest({OBOracleSessionExtension.class, JdbcOperationsUtil.class})
public class OBOracleSessionExtensionTest {

    private static final String DATASOURCE_URL = "jdbc:oceanbase:oozie:12345/oceanbase";
    private static final String DRIVER_CLASS = "com.oceanbase.jdbc.Driver";
    private Connection connection;
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setup() throws Exception {
        Class.forName(DRIVER_CLASS);
        ExtensionManager extensionManager = new ExtensionManager();
        PluginWrapper pluginWrapper = extensionManager.getPlugin(OBOracleSessionExtension.class);
        obOracleSessionExtension = (OBOracleSessionExtension) pluginWrapper.getLoadedInstance();
        connection = DriverManager.getConnection(DATASOURCE_URL, "user", "password");
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testKillQuery() {
        obOracleSessionExtension.killQuery(connection, "connectionId");
        // Add an assertion here to check the functionality of the killQuery method
    }

    @Test
    public void testSwitchSchema_WhenSchemaAlreadySet_ThenDoNothing() {
        String schemaName = "testSchema";
        obOracleSessionExtension.switchSchema(connection, schemaName);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        // Add an assertion here to check the functionality of the switchSchema method when schema already set
    }

    @Test
    public void testSwitchSchema_WhenSwitchToSameSchema_ThenDoNothing() {
        String schemaName = "testSchema";
        obOracleSessionExtension.switchSchema(connection, schemaName);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertSame(schemaName, currentSchema);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        // Add an assertion here to check the functionality of the switchSchema method when switch to same schema
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_WhenThrowException_ThenThrowSQLException() {
        String schemaName = "testSchema";
        String querySql = "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenThrow(new SQLException("Test Exception"));
        obOracleSessionExtension.switchSchema(connection, schemaName);
        // Add an assertion here to check the exception handling of the switchSchema method
    }

    @Test
    public void testSwitchSchema_WhenSuccessfullySwitchSchema_ThenGetCorrectSchemaName() {
        String schemaName = "testSchema";
        obOracleSessionExtension.switchSchema(connection, schemaName);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertSame(schemaName, currentSchema);
        // Add an assertion here to check the functionality of the getCurrentSchema method after successful switch schema
    }

    @Test
    public void testGetConnectionId_WhenGetConnectionIdFromConnection_ThenReturnConnectionId() {
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        // Add an assertion here to check the functionality of the getConnectionId method when get connection id from connection
    }

    @Test
    public void testGetConnectionId_WhenGetConnectionIdFromOceanBaseConnection_ThenReturnServerThreadId() {
        OceanBaseConnection oceanBaseConnection = new OceanBaseConnection(connection);
        String connectionId = obOracleSessionExtension.getConnectionId(oceanBaseConnection);
        // Add an assertion here to check the functionality of the getConnectionId method when get connection id from OceanBaseConnection
    }

    @Test
    public void testGetConnectionId_WhenGetNullFromConnection_ThenReturnNull() {
        connection = null;
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertNull(connectionId);
        // Add an assertion here to check the functionality of the getConnectionId method when get null from connection
    }

    @Test
    public void testGetCurrentSchema_WhenGetSchemaNameFromConnection_ThenReturnSchemaName() {
        String schemaName = "testSchema";
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn(schemaName);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertSame(schemaName, currentSchema);
        // Add an assertion here to check the functionality of the getCurrentSchema method when get schema name from connection
    }

    @Test
    public void testGetCurrentSchema_WhenGetNullFromConnection_ThenReturnNull() {
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn(null);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertNull(currentSchema);
        // Add an assertion here to check the functionality of the getCurrentSchema method when get null from connection
    }
}