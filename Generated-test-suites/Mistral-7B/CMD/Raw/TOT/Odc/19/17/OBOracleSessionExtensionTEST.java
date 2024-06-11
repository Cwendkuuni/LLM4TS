package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionManager;
import org.pf4j.ExtensionPoint;
import org.pf4j.impl.DefaultExtensionManager;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

/**
 * Test class for {@link OBOracleSessionExtension}.
 *
 * @author yaobin
 */
public class OBOracleSessionExtensionTest {

    private Connection connection;
    private ExtensionManager extensionManager;
    private SessionExtensionPoint sessionExtension;

    @Before
    public void setUp() throws Exception {
        extensionManager = new DefaultExtensionManager();
        extensionManager.loadExtensions("classpath:META-INF/extensions.xml");
        sessionExtension = (SessionExtensionPoint) extensionManager.getExtension(OBOracleSessionExtension.class);
        connection = JdbcOperationsUtil.getConnection();
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = sessionExtension.getConnectionId(connection);
        sessionExtension.killQuery(connection, connectionId);
        assertTrue(true); // Killing query should not throw any exception.
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithInvalidConnectionId() throws SQLException {
        String invalidConnectionId = "12345";
        sessionExtension.killQuery(connection, invalidConnectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String newSchemaName = "testSchema";
        sessionExtension.switchSchema(connection, newSchemaName);
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        assertEquals(newSchemaName, currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithInvalidSchemaName() throws SQLException {
        String invalidSchemaName = "";
        sessionExtension.switchSchema(connection, invalidSchemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        assertNotNull(currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = sessionExtension.getConnectionId(connection);
        assertNotNull(connectionId);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionIdWithNullConnection() throws SQLException {
        sessionExtension.getConnectionId(null);
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        Optional<SessionExtensionPoint> sessionExtensionOptional = Optional.ofNullable(sessionExtension);
        SessionExtensionPoint sessionExtensionWrapped = sessionExtensionOptional.isPresent() ? sessionExtension : new OBOracleSessionExtension();
        String connectionId = sessionExtensionWrapped.getConnectionId(new OceanBaseConnection());
        assertNotNull(connectionId);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionIdWithWrongConnectionType() throws SQLException {
        String connectionId = sessionExtension.getConnectionId((Connection) new Object());
    }

    @After
    public void tearDown() throws Exception {
        JdbcOperationsUtil.closeConnection(connection);
        extensionManager.unloadExtensions();
    }
}