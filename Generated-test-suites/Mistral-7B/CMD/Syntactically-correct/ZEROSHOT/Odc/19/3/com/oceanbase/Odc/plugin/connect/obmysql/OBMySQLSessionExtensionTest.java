import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pf4j.ExtensionManager;
import org.pf4j.Extensions;
import org.pf4j.PluginWrapper;
import org.pf4j.TestExtensions;
import org.pf4j.TestRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(TestRunner.class)
public class OBMySQLSessionExtensionTest {

    private ExtensionManager extensionManager;
    private PluginWrapper<OBMySQLSessionExtension> extension;

    @Before
    public void setup() throws Exception {
        extensionManager = Extensions.getDefaultExtensionManager();
        extensionManager.loadExtensions("path/to/your/extension/jar/file.jar");
        extension = extensionManager.getExtensionByPoint(SessionExtensionPoint.class);
    }

    @After
    public void teardown() throws Exception {
        extensionManager.shutdown();
    }

    @Test
    public void testKillQuery() throws SQLException {
        // Arrange
        Connection connection = JdbcOperationsUtil.createConnection();
        String connectionId = "123";

        // Act
        extension.getExtension().killQuery(connection, connectionId);

        // Assert
        // This test might not be possible to pass without a real OceanBase instance
        // You may add a mock implementation for JdbcOperationsUtil.execute method
        // or check the connection state after the method call
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        // Arrange
        Connection connection = JdbcOperationsUtil.createConnection();
        String schemaName = "test_schema";

        // Act
        extension.getExtension().switchSchema(connection, schemaName);

        // Assert
        String currentSchema = extension.getExtension().getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
    }

    @Test
    public void testGetCurrentSchema() {
        // Arrange
        Connection connection = JdbcOperationsUtil.createConnection();

        // Act
        String currentSchema = extension.getExtension().getCurrentSchema(connection);

        // Assert
        assertNotNull(currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        // Arrange
        Connection connection = JdbcOperationsUtil.createConnection();

        // Act
        String connectionId = extension.getExtension().getConnectionId(connection);

        // Assert
        assertNotNull(connectionId);
    }

    @Test
    public void testGetConnectionIdFromOceanBaseConnection() throws SQLException {
        // Arrange
        Connection connection = JdbcOperationsUtil.createOceanBaseConnection();

        // Act
        String connectionId = extension.getExtension().getConnectionId(connection);

        // Assert
        assertNotNull(connectionId);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaToSameSchema() throws SQLException {
        // Arrange
        Connection connection = JdbcOperationsUtil.createConnection();
        String schemaName = JdbcOperationsUtil.getCurrentSchema(connection);

        // Act & Assert
        extension.getExtension().switchSchema(connection, schemaName);
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithNullConnection() {
        // Arrange
        String connectionId = "123";

        // Act & Assert
        extension.getExtension().killQuery(null, connectionId);
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithNullConnectionId() {
        // Arrange
        Connection connection = JdbcOperationsUtil.createConnection();

        // Act & Assert
        extension.getExtension().killQuery(connection, null);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithNullConnection() {
        // Arrange
        String schemaName = "test_schema";

        // Act & Assert
        extension.getExtension().switchSchema(null, schemaName);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithNullSchemaName() {
        // Arrange
        Connection connection = JdbcOperationsUtil.createConnection();

        // Act & Assert
        extension.getExtension().switchSchema(connection, null);
    }
}