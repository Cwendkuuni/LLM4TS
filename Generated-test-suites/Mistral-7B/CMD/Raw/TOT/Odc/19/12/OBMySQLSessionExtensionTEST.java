package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionManager;
import org.pf4j.impl.DefaultExtensionManager;
import org.pf4j.impl.DefaultExtensionPoint;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

/**
 * Test class for {@link OBMySQLSessionExtension}.
 */
public class OBMySQLSessionExtensionTest {

    private static ExtensionManager extensionManager;
    private static OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setup() {
        extensionManager = new DefaultExtensionManager();
        extensionManager.loadExtensions("path/to/extension/jar");
        obMySQLSessionExtension = (OBMySQLSessionExtension) extensionManager.getExtensionByType(SessionExtensionPoint.class);
    }

    @After
    public void teardown() {
        extensionManager = null;
        obMySQLSessionExtension = null;
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = createConnection();
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        obMySQLSessionExtension.killQuery(connection, connectionId);
        assertFalse("Query should be killed", JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT 1 FROM DUAL", Boolean.class));
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithInvalidConnectionId() throws SQLException {
        Connection connection = createConnection();
        obMySQLSessionExtension.killQuery(connection, "invalidId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = createConnection();
        String oldSchema = getCurrentSchema(connection);

        obMySQLSessionExtension.switchSchema(connection, "testSchema");
        String newSchema = getCurrentSchema(connection);
        assertNotEquals("Schema should be switched", oldSchema, newSchema);

        obMySQLSessionExtension.switchSchema(connection, oldSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithInvalidSchemaName() throws SQLException {
        Connection connection = createConnection();
        obMySQLSessionExtension.switchSchema(connection, "");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = createConnection();
        String schema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertNotNull("Current schema should not be null", schema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = createConnection();
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertNotNull("Connection id should not be null", connectionId);
    }

    @Test
    public void testGetConnectionIdForOceanBaseConnection() throws SQLException {
        Connection connection = new OceanBaseConnection(null);
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertNotNull("Connection id should not be null", connectionId);
    }

    private Connection createConnection() throws SQLException {
        return JdbcOperationsUtil.getJdbcOperations().getConnection("jdbc:oceanbase:ob://localhost:2880/testDB");
    }

    private String getCurrentSchema(Connection connection) throws SQLException {
        return obMySQLSessionExtension.getCurrentSchema(connection);
    }
}