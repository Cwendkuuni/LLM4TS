package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.impl.DefaultExtension;
import org.pf4j.impl.FileSystemExtensionLoader;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.JdbcOperationsUtil;

/**
 * Test class for OBMySQLSessionExtension
 *
 * @author yaobin
 * @since 4.2.0
 */
@SuppressWarnings("DefaultFileTemplateClass")
public class OBMySQLSessionExtensionTest {

    private SessionExtensionPoint sessionExtension;

    @Before
    public void init() throws Exception {
        FileSystemExtensionLoader loader = new FileSystemExtensionLoader(OBMySQLSessionExtension.class);
        sessionExtension = (SessionExtensionPoint) loader.load();
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = null;
        String connectionId = "test_connection_id";
        sessionExtension.killQuery(connection, connectionId);
        assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithNullConnection() {
        String connectionId = "test_connection_id";
        sessionExtension.killQuery(null, connectionId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKillQueryWithNullConnectionId() {
        Connection connection = null;
        sessionExtension.killQuery(connection, null);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = null;
        String schemaName = "test_schema";
        sessionExtension.switchSchema(connection, schemaName);
        assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithNullConnection() {
        String schemaName = "test_schema";
        sessionExtension.switchSchema(null, schemaName);
    }

    @Test
    public void testSwitchSchemaWithEmptySchemaName() throws SQLException {
        Connection connection = null;
        String schemaName = "";
        sessionExtension.switchSchema(connection, schemaName);
        assertNotNull(connection);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = null;
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        assertNotNull(currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testGetCurrentSchemaWithNullConnection() {
        sessionExtension.getCurrentSchema(null);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = null;
        String connectionId = sessionExtension.getConnectionId(connection);
        assertNotNull(connectionId);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionIdWithNullConnection() {
        sessionExtension.getConnectionId(null);
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        Connection connection = new OceanBaseConnection("jdbc:obmysql://localhost:2880/testdb", "", "");
        String connectionId = sessionExtension.getConnectionId(connection);
        assertNotNull(connectionId);
        assertEquals("testdb", connection.getCatalog());
    }
}