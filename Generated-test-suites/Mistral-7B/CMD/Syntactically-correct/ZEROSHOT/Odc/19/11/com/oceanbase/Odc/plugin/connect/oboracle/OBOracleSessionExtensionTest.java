package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionPoints;
import org.pf4j.ExtensionRegistry;
import org.pf4j.impl.DefaultExtensionPoint;
import org.pf4j.impl.DefaultExtensionRegistry;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@SuppressWarnings("unchecked")
public class OBOracleSessionExtensionTest {

    private static ExtensionRegistry registry;
    private static OBOracleSessionExtension obOracleSessionExtension;
    private Connection connection;

    @Before
    public void beforeEachTest() throws Exception {
        registry = new DefaultExtensionRegistry();
        ExtensionPoints<SessionExtensionPoint> extensionPoints = new DefaultExtensionPoint<>(SessionExtensionPoint.class);
        registry.getExtensionManager().registerExtensions(extensionPoints);
        obOracleSessionExtension = (OBOracleSessionExtension) registry.getExtensionManager().getExtensions(SessionExtensionPoint.class).get(0);
        connection = JdbcOperationsUtil.getConnection("jdbc:oceanbase:observer:127.0.0.1:2881/oceanbase:1521/testDatabase");
    }

    @After
    public void afterEachTest() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "test_connection_id";
        obOracleSessionExtension.killQuery(connection, connectionId);
        // Verification goes here, e.g. using a statement counter or a query result set
        // This test case is not complete as it does not verify the result of killing the query
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        obOracleSessionExtension.switchSchema(connection, schemaName);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaInvalidSchemaName() throws SQLException {
        String schemaName = null;
        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertNotNull(currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Optional<String> connectionId = Optional.ofNullable(obOracleSessionExtension.getConnectionId(connection));
        assertTrue(connectionId.isPresent());
        assertEquals(connectionId.get(), getConnectionIdFromConnection(connection));
    }

    @Test
    public void testGetConnectionIdFromInvalidConnection() {
        assertNull(obOracleSessionExtension.getConnectionId(null));
    }

    private String getConnectionIdFromConnection(Connection connection) {
        if (connection instanceof OceanBaseConnection) {
            return ((OceanBaseConnection) connection).getServerThreadId() + "";
        }
        return null;
    }
}