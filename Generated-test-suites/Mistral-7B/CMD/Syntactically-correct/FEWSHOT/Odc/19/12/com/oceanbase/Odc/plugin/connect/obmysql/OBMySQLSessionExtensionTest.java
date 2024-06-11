package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Context;
import org.pf4j.ExtensionManager;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.jdbc.impl.ConnectionProxy;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

public class OBMySQLSessionExtensionTest {

    private SessionExtensionPoint sessionExtension;
    private Context context;
    private ExtensionManager extensionManager;

    @Before
    public void setup() {
        context = new Context();
        extensionManager = context.getExtensionManager();
        sessionExtension = new OBMySQLSessionExtension();
        extensionManager.addExtension(sessionExtension);
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = createOceanBaseConnection();
        String connectionId = "12345";
        sessionExtension.killQuery(connection, connectionId);
        assertNull(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SHOW PROCESSLIST WHERE ID = " + connectionId, String.class));
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithNullConnection() {
        sessionExtension.killQuery(null, "12345");
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithNullConnectionId() {
        Connection connection = createOceanBaseConnection();
        sessionExtension.killQuery(connection, null);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = createOceanBaseConnection();
        String schemaName = "test_schema";
        sessionExtension.switchSchema(connection, schemaName);
        assertEquals(schemaName, JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithNullConnection() {
        sessionExtension.switchSchema(null, "test_schema");
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithNullSchemaName() {
        Connection connection = createOceanBaseConnection();
        sessionExtension.switchSchema(connection, null);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = createOceanBaseConnection();
        assertEquals("default_schema", JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class));
    }

    @Test(expected = SQLException.class)
    public void testGetCurrentSchemaWithNullConnection() {
        sessionExtension.getCurrentSchema(null);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = createOceanBaseConnection();
        String connectionId = sessionExtension.getConnectionId(connection);
        assertNotNull(connectionId);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionIdWithNullConnection() {
        sessionExtension.getConnectionId(null);
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        Connection connection = createOceanBaseConnection();
        String connectionId = sessionExtension.getConnectionId(connection);
        assertEquals(((OceanBaseConnection) connection).getServerThreadId(), connectionId);
    }

    @Test
    public void testGetConnectionIdWithJdbcConnection() throws SQLException {
        Connection connection = new ConnectionProxy(createOceanBaseConnection());
        String connectionId = sessionExtension.getConnectionId(connection);
        assertNull(connectionId);
    }

    private Connection createOceanBaseConnection() throws SQLException {
        return new OceanBaseConnection("jdbc:oceanbase:obcds://localhost:2880/oceanbase");
    }
}