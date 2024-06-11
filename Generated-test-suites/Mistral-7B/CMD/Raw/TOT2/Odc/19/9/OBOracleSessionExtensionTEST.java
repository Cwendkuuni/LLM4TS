package com.oceanbase.odc.plugin.connect.oboracle.test;

import org.junit.jupiter.api.*;
import org.mockito.Mockito.*;
import org.pf4j.*;

import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

@Extension
@Slf4j
public class OBOracleSessionExtensionTest {

    private static OBOracleSessionExtension obOracleSessionExtension;
    private static Connection connection;

    @BeforeAll
    public static void setUp() throws Exception {
        PluginWrapper pluginWrapper = new PluginWrapper();
        obOracleSessionExtension = new OBOracleSessionExtension();
        ExtensionManager extensionManager = ExtensionManager.getInstance();
        extensionManager.loadExtensions();
        ExtensionLoader extensionLoader = ExtensionLoader.getInstance();
        obOracleSessionExtension = (OBOracleSessionExtension) extensionLoader.getExtensionByName("OBOracleSessionExtension");
        connection = Mockito.mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws Exception {
        // Test case 1: Basic test
        obOracleSessionExtension.killQuery(connection, "connectionId1");
        // Test case 2: Edge case: SQLException is thrown
        Connection connectionWithException = Mockito.mock(Connection.class);
        Mockito.when(connectionWithException.createStatement()).thenThrow(SQLException.class);
        obOracleSessionExtension.killQuery(connectionWithException, "connectionId2");
        // Test case 3: Edge case: ConnectionId is null
        obOracleSessionExtension.killQuery(null, "connectionId3");
        // Test case 4: Edge case: ConnectionId is empty
        obOracleSessionExtension.killQuery(connection, "");
    }

    @Test
    public void testSwitchSchema() throws Exception {
        // Test case 1: Basic test
        obOracleSessionExtension.switchSchema(connection, "schemaName1");
        // Test case 2: Edge case: SQLException is thrown
        Connection connectionWithException = Mockito.mock(Connection.class);
        Mockito.when(connectionWithException.createStatement()).thenThrow(SQLException.class);
        obOracleSessionExtension.switchSchema(connectionWithException, "schemaName2");
        // Test case 3: Edge case: SchemaName is null
        obOracleSessionExtension.switchSchema(connection, null);
        // Test case 4: Edge case: SchemaName is empty
        obOracleSessionExtension.switchSchema(connection, "");
        // Test case 5: Edge case: Current schema is the same as schemaName
        String currentSchema = "schemaName1";
        Mockito.when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);
        obOracleSessionExtension.switchSchema(connection, "schemaName1");
    }

    @Test
    public void testGetCurrentSchema() throws Exception {
        // Test case 1: Basic test
        Mockito.when(connection.createStatement()).thenReturn(Mockito.mock(Statement.class));
        Mockito.when(connection.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
                .thenReturn("schemaName1");
        assertEquals("schemaName1", obOracleSessionExtension.getCurrentSchema(connection));
        // Test case 2: Edge case: No result set
        Mockito.when(connection.createStatement()).thenReturn(Mockito.mock(Statement.class));
        Mockito.when(connection.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
                .thenReturn(null);
        assertNull(obOracleSessionExtension.getCurrentSchema(connection));
        // Test case 3: Edge case: SQLException is thrown
        Connection connectionWithException = Mockito.mock(Connection.class);
        Mockito.when(connectionWithException.createStatement()).thenThrow(SQLException.class);
        assertThrows(SQLException.class, () -> obOracleSessionExtension.getCurrentSchema(connectionWithException));
    }

    @AfterAll
    public static void tearDown() throws Exception {
        ExtensionManager extensionManager = ExtensionManager.getInstance();
        extensionManager.shutdown();
    }
}