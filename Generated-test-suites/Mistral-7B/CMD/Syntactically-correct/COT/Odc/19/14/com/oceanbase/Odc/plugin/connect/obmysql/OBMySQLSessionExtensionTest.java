package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pf4j.ExtensionManager;
import org.pf4j.ExtensionManagerFactory;
import org.pf4j.TestExtension;
import org.pf4j.lifecycle.LifecycleManager;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.obmysql.test.util.TestConnectionFactory;

@RunWith(TestExtension.class)
public class OBMySQLSessionExtensionTest {

    private static final String TEST_SCHEMA = "test_schema";
    private Connection connection;
    private OBMySQLSessionExtension extension;
    private ExtensionManager extensionManager;
    private LifecycleManager lifecycleManager;

    @Before
    public void setup() throws Exception {
        extensionManager = ExtensionManagerFactory.getExtensions(SessionExtensionPoint.class);
        lifecycleManager = extensionManager.getLifecycleManager();
        connection = TestConnectionFactory.getTestConnection();
        extension = (OBMySQLSessionExtension) extensionManager.getExtensionByName("OBMySQLSessionExtension");
        lifecycleManager.start(extension);
    }

    @After
    public void teardown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        lifecycleManager.stop(extension);
    }

    @Test
    public void testKillQuery() throws Exception {
        String connectionId = extension.getConnectionId(connection);
        extension.killQuery(connection, connectionId);
        assertThrows(SQLException.class, () -> JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SHOW PROCESSLIST", Object[].class));
    }

    @Test
    public void testSwitchSchema() throws Exception {
        String currentSchema = extension.getCurrentSchema(connection);
        extension.switchSchema(connection, TEST_SCHEMA);
        String newSchema = extension.getCurrentSchema(connection);
        assertEquals(TEST_SCHEMA, newSchema);
        extension.switchSchema(connection, currentSchema);
        assertEquals(currentSchema, newSchema);
    }

    @Test
    public void testGetCurrentSchema() throws Exception {
        assertNotNull(extension.getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testGetCurrentSchemaWhenException() throws Exception {
        connection.createStatement().execute("DROP DATABASE IF EXISTS test_schema");
        assertThrows(SQLException.class, () -> extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws Exception {
        assertNotNull(extension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWhenNoConnectionId() throws Exception {
        assertNull(extension.getConnectionId(connection));
    }
}