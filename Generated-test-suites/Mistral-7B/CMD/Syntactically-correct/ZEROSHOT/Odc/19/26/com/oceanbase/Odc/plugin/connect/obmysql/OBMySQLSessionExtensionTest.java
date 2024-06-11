import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;
import org.pf4j.TestExtension;
import org.pf4j.junit.Pf4JExtension;
import org.pf4j.util.Extensions;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(Pf4JExtension.class)
public class OBMySQLSessionExtensionTest {

    private ExtensionManager extensionManager;
    private PluginWrapper pluginWrapper;
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() {
        extensionManager = new ExtensionManager();
        extensionManager.scanClassPathForExtensions();
        pluginWrapper = extensionManager.getExtensionById("OBMySQLSessionExtension");
        obMySQLSessionExtension = (OBMySQLSessionExtension) Extensions.getExtension(PluginWrapper.class, pluginWrapper.getId()).getComponentInstance();
    }

    @After
    public void tearDown() {
        if (obMySQLSessionExtension != null) {
            obMySQLSessionExtension = null;
        }
        if (pluginWrapper != null) {
            pluginWrapper = null;
        }
        if (extensionManager != null) {
            extensionManager = null;
        }
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = JdbcOperationsUtil.getTestConnection();
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        String schemaName = "test_schema";

        try {
            obMySQLSessionExtension.killQuery(connection, connectionId);
            // You can add assertions here to test the effect of the "KILL QUERY" command
        } finally {
            JdbcOperationsUtil.closeConnection(connection);
        }

        connection = JdbcOperationsUtil.getTestConnection();
        try {
            obMySQLSessionExtension.switchSchema(connection, schemaName);
            obMySQLSessionExtension.killQuery(connection, connectionId);
            String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
            assertEquals("test_schema", currentSchema);
            JdbcOperationsUtil.closeConnection(connection);
        } finally {
            obMySQLSessionExtension.switchSchema(connection, getCurrentSchema(connection));
            JdbcOperationsUtil.closeConnection(connection);
        }
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = JdbcOperationsUtil.getTestConnection();
        String schemaName1 = "test_schema_1";
        String schemaName2 = "test_schema_2";

        try {
            obMySQLSessionExtension.switchSchema(connection, schemaName1);
            String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
            assertEquals(schemaName1, currentSchema);

            obMySQLSessionExtension.switchSchema(connection, schemaName2);
            currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
            assertEquals(schemaName2, currentSchema);

            JdbcOperationsUtil.closeConnection(connection);
        } finally {
            obMySQLSessionExtension.switchSchema(connection, getCurrentSchema(connection));
            JdbcOperationsUtil.closeConnection(connection);
        }
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        Connection connection = JdbcOperationsUtil.getTestConnection();
        String schemaName = "test_schema";

        try {
            obMySQLSessionExtension.switchSchema(connection, schemaName);
            String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
            assertEquals(getCurrentSchema(connection), currentSchema);

            throw new SQLException("Test exception");
        } finally {
            obMySQLSessionExtension.switchSchema(connection, getCurrentSchema(connection));
            JdbcOperationsUtil.closeConnection(connection);
        }
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = JdbcOperationsUtil.getTestConnection();
        String currentSchema = getCurrentSchema(connection);

        try {
            obMySQLSessionExtension.switchSchema(connection, "test_schema");
            String newSchema = obMySQLSessionExtension.getCurrentSchema(connection);
            assertEquals("test_schema", newSchema);

            obMySQLSessionExtension.switchSchema(connection, currentSchema);
            String restoredSchema = obMySQLSessionExtension.getCurrentSchema(connection);
            assertEquals(currentSchema, restoredSchema);

            JdbcOperationsUtil.closeConnection(connection);
        } finally {
            obMySQLSessionExtension.switchSchema(connection, getCurrentSchema(connection));
            JdbcOperationsUtil.closeConnection(connection);
        }
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = JdbcOperationsUtil.getTestConnection();
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);

        try {
            assertNull(connectionId);
            if (connection instanceof OceanBaseConnection) {
                connectionId = ((OceanBaseConnection) connection).getServerThreadId() + "";
                assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(connection));
            }
            JdbcOperationsUtil.closeConnection(connection);
        } finally {
            obMySQLSessionExtension.switchSchema(connection, getCurrentSchema(connection));
            JdbcOperationsUtil.closeConnection(connection);
        }
    }

    private String getCurrentSchema(Connection connection) {
        try {
            return obMySQLSessionExtension.getCurrentSchema(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}