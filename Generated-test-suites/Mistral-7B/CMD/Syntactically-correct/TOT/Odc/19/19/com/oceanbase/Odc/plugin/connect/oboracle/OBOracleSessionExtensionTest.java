package com.oceanbase.odc.plugin.connect.oboracle.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionManager;
import org.pf4j.TestExtension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class OBOracleSessionExtensionTest {

    private Connection connection;
    private SessionExtensionPoint sessionExtensionPoint;

    @Before
    public void setUp() throws Exception {
        ExtensionManager extensionManager = new ExtensionManager("com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension");
        sessionExtensionPoint = extensionManager.getExtensions(SessionExtensionPoint.class).get(0);
        connection = sessionExtensionPoint.createConnection();
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = sessionExtensionPoint.getConnectionId(connection);
        sessionExtensionPoint.killQuery(connection, connectionId);
        assertThrows(SQLException.class, () -> connection.createStatement().executeQuery("SELECT COUNT(*) FROM DUAL"));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String currentSchema = sessionExtensionPoint.getCurrentSchema(connection);
        sessionExtensionPoint.switchSchema(connection, "test_schema");
        assertEquals("test_schema", sessionExtensionPoint.getCurrentSchema(connection));
        sessionExtensionPoint.switchSchema(connection, currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaAlreadyInSchema() throws SQLException {
        String currentSchema = sessionExtensionPoint.getCurrentSchema(connection);
        sessionExtensionPoint.switchSchema(connection, currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId1 = sessionExtensionPoint.getConnectionId(connection);
        String connectionId2 = sessionExtensionPoint.getConnectionId(connection);
        assertEquals(connectionId1, connectionId2);
        if (connection instanceof OceanBaseConnection) {
            OceanBaseConnection oceanBaseConnection = (OceanBaseConnection) connection;
            String serverThreadId1 = oceanBaseConnection.getServerThreadId();
            String serverThreadId2 = ((OceanBaseConnection) connection).getServerThreadId();
            assertNotEquals(serverThreadId1, serverThreadId2);
        }
    }

    @Test
    public void testGetCurrentSchemaNullConnection() {
        assertNull(sessionExtensionPoint.getCurrentSchema(null));
    }

    @Test(expected = SQLException.class)
    public void testGetCurrentSchemaWrongConnection() throws SQLException {
        Connection wrongConnection = sessionExtensionPoint.createConnection();
        assertThrows(SQLException.class, () -> sessionExtensionPoint.getCurrentSchema(wrongConnection));
    }

    @After
    public void tearDown() throws Exception {
        sessionExtensionPoint.closeConnection(connection);
    }
}