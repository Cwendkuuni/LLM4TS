package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.TestExtension;
import org.pf4j.TestExtensionManager;
import org.pf4j.util.ClassPathScanner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@SuppressWarnings("unchecked")
public class OBOracleSessionExtensionTest {

    private SessionExtensionPoint obOracleSessionExtension;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        ExtensionManager extensionManager = new TestExtensionManager();
        ClassPathScanner classPathScanner = new ClassPathScanner();
        classPathScanner.scanPackagesOfClass("com.oceanbase.odc.plugin.connect.oboracle");
        for (Extension extension : classPathScanner.getExtensionsOfType(SessionExtensionPoint.class)) {
            extensionManager.addExtension((TestExtension) extension);
        }
        obOracleSessionExtension = (OBOracleSessionExtension) extensionManager.getExtension("OBOracleSessionExtension");
        connection = null;
    }

    // Test killQuery method
    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "test_connection_id";
        Connection testConnection = OBOracleSessionExtensionTest.getTestConnection();
        obOracleSessionExtension.killQuery(testConnection, connectionId);
        // TODO: Add assertion to check if query has been killed
    }

    // Test switchSchema method with valid schema name
    @Test
    public void testSwitchSchemaValidSchemaName() throws SQLException {
        String schemaName = "test_schema";
        Connection testConnection = OBOracleSessionExtensionTest.getTestConnection();
        obOracleSessionExtension.switchSchema(testConnection, schemaName);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(testConnection);
        assertEquals(schemaName, currentSchema);
    }

    // Test switchSchema method with same schema name
    @Test
    public void testSwitchSchemaSameSchemaName() throws SQLException {
        String schemaName = "test_schema";
        Connection testConnection = OBOracleSessionExtensionTest.getTestConnection();
        obOracleSessionExtension.switchSchema(testConnection, schemaName);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(testConnection);
        assertEquals(schemaName, currentSchema);
    }

    // Test switchSchema method with invalid schema name
    @Test(expected = SQLException.class)
    public void testSwitchSchemaInvalidSchemaName() throws SQLException {
        String invalidSchemaName = "";
        Connection testConnection = OBOracleSessionExtensionTest.getTestConnection();
        obOracleSessionExtension.switchSchema(testConnection, invalidSchemaName);
    }

    // Test getCurrentSchema method
    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection testConnection = OBOracleSessionExtensionTest.getTestConnection();
        String currentSchema = obOracleSessionExtension.getCurrentSchema(testConnection);
        assertNotNull(currentSchema);
    }

    // Test getConnectionId method with valid connection
    @Test
    public void testGetConnectionIdValidConnection() throws SQLException {
        Connection testConnection = OBOracleSessionExtensionTest.getTestConnection();
        String connectionId = obOracleSessionExtension.getConnectionId(testConnection);
        assertNotNull(connectionId);
    }

    // Test getConnectionId method with null connection
    @Test
    public void testGetConnectionIdNullConnection() {
        String connectionId = obOracleSessionExtension.getConnectionId(null);
        assertNull(connectionId);
    }

    // Test getConnectionId method with OceanBaseConnection
    @Test
    public void testGetConnectionIdOceanBaseConnection() throws SQLException {
        Connection testConnection = (Connection) new OceanBaseConnection("jdbc:obcds://localhost:1527/testdb", "user", "password");
        String connectionId = obOracleSessionExtension.getConnectionId(testConnection);
        assertNotNull(connectionId);
    }

    private static Connection getTestConnection() throws SQLException {
        return JdbcOperationsUtil.getTestConnection();
    }
}