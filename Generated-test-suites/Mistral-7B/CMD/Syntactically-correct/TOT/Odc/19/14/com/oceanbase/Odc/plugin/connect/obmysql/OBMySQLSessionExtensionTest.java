package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.pf4j.ExtensionManager;
import org.pf4j.ExtensionPoint;
import org.pf4j.TestExtension;
import org.pf4j.TestExtensionManager;
import org.pf4j.util.ClasspathScanner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.JdbcOperationsUtil;

public class OBMySQLSessionExtensionTest {

    private ExtensionPoint<SessionExtensionPoint> sessionExtensionPoint;

    @Rule
    public TestExtensionManager testExtensionManager = new TestExtensionManager();

    @Before
    public void setup() throws Exception {
        ExtensionManager extensionManager = testExtensionManager.getExtensionManager();
        ClasspathScanner scanner = new ClasspathScanner(OBMySQLSessionExtensionTest.class.getClassLoader());
        extensionManager.scan(scanner);
        sessionExtensionPoint = extensionManager.getExtensions(SessionExtensionPoint.class).get(0);
    }

    @Test
    public void testKillQuery() throws Exception {
        Connection connection = getTestConnection();
        String connectionId = "test_query";
        sessionExtensionPoint.killQuery(connection, connectionId);
        // Add assertions for successful query kill here
    }

    @Test
    public void testKillQueryWithInvalidConnectionId() throws Exception {
        Connection connection = getTestConnection();
        String connectionId = "invalid_query";
        assertThrows(SQLException.class, () -> sessionExtensionPoint.killQuery(connection, connectionId));
    }

    @Test
    public void testKillQueryWithNullConnection() {
        assertThrows(NullPointerException.class, () -> sessionExtensionPoint.killQuery(null, "test_query"));
    }

    @Test
    public void testSwitchSchema() throws Exception {
        Connection connection = getTestConnection();
        String schemaName = "test_schema";
        sessionExtensionPoint.switchSchema(connection, schemaName);
        String currentSchema = sessionExtensionPoint.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
    }

    @Test
    public void testSwitchSchemaWithInvalidSchemaName() throws Exception {
        Connection connection = getTestConnection();
        String schemaName = "";
        assertThrows(SQLException.class, () -> sessionExtensionPoint.switchSchema(connection, schemaName));
    }

    @Test
    public void testSwitchSchemaWithNullConnection() {
        assertThrows(NullPointerException.class, () -> sessionExtensionPoint.switchSchema(null, "test_schema"));
    }

    @Test
    public void testgetCurrentSchema() throws Exception {
        Connection connection = getTestConnection();
        String currentSchema = sessionExtensionPoint.getCurrentSchema(connection);
        // Add assertions for correct current schema here
    }

    @Test
    public void testgetCurrentSchemaWithNullConnection() {
        assertThrows(NullPointerException.class, () -> sessionExtensionPoint.getCurrentSchema(null));
    }

    @Test
    public void testgetConnectionId() throws Exception {
        Connection connection = getTestConnection();
        String connectionId = sessionExtensionPoint.getConnectionId(connection);
        // Add assertions for correct connection id here
    }

    @Test
    public void testgetConnectionIdWithNullConnection() {
        assertThrows(NullPointerException.class, () -> sessionExtensionPoint.getConnectionId(null));
    }

    private Connection getTestConnection() throws SQLException {
        OceanBaseConnection connection = new OceanBaseConnection("jdbc:obmysql://localhost:1527/test_db");
        return connection;
    }
}