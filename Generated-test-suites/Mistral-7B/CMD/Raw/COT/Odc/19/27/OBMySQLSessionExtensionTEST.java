package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionFactory;
import org.pf4j.TestExtensionFactory;
import org.pf4j.util.ComponentUtils;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

/**
 * Test class for OBMySQLSessionExtension.
 */
public class OBMySQLSessionExtensionTest {

    private SessionExtensionPoint obMySQLSessionExtension;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        ExtensionFactory extensionFactory = new TestExtensionFactory();
        obMySQLSessionExtension = (SessionExtensionPoint) extensionFactory.getExtension("OBMySQLSessionExtension");
        connection = JdbcOperationsUtil.getConnection();
    }

    @Test
    public void testKillQuery() {
        obMySQLSessionExtension.killQuery(connection, "testConnectionId");
    }

    @Test
    public void testKillQueryWithNullConnection() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.killQuery(null, "testConnectionId"));
    }

    @Test
    public void testKillQueryWithEmptyConnectionId() {
        assertThrows(IllegalArgumentException.class, () -> obMySQLSessionExtension.killQuery(connection, ""));
    }

    @Test
    public void testSwitchSchema() {
        obMySQLSessionExtension.switchSchema(connection, "testSchema");
    }

    @Test
    public void testSwitchSchemaWithNullConnection() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.switchSchema(null, "testSchema"));
    }

    @Test
    public void testSwitchSchemaWithNullSchemaName() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.switchSchema(connection, null));
    }

    @Test
    public void testSwitchSchemaWithSameSchemaName() {
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        obMySQLSessionExtension.switchSchema(connection, currentSchema);
    }

    @Test
    public void testGetCurrentSchema() {
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertNotNull(currentSchema);
    }

    @Test
    public void testGetCurrentSchemaWithNullConnection() {
        assertNull(obMySQLSessionExtension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionId() {
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals(connectionId, getConnectionIdFromConnection(connection));
    }

    @Test
    public void testGetConnectionIdWithNullConnection() {
        assertNull(obMySQLSessionExtension.getConnectionId(null));
    }

    private String getConnectionIdFromConnection(Connection connection) {
        if (connection instanceof OceanBaseConnection) {
            return ((OceanBaseConnection) connection).getServerThreadId() + "";
        }
        return null;
    }

    @Test(expected = SQLException.class)
    public void testExecuteQueryWhenExceptionThrown() throws SQLException {
        Objects.requireNonNull(connection, "connection can't be null.");
        String querySql = "invalid query";
        obMySQLSessionExtension.switchSchema(connection, "testSchema");
        obMySQLSessionExtension.killQuery(connection, "testConnectionId");
        assertThrows(SQLException.class, () -> JdbcOperationsUtil.getJdbcOperations(connection).execute(querySql));
    }
}