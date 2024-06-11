import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.pf4j.ExtensionManager;
import org.pf4j.impl.DefaultExtensionManager;
import org.pf4j.impl.DefaultExtensionPoint;
import org.pf4j.impl.annotations.Extension;
import org.pf4j.impl.annotations.ExtensionPoint;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("unchecked")
public class OBOracleSessionExtensionTest {

    private ExtensionManager extensionManager;
    private DefaultExtensionPoint<SessionExtensionPoint> sessionExtensionPoint;

    @Before
    public void setUp() {
        extensionManager = new DefaultExtensionManager();
        extensionManager.scanPackagesOf(OBOracleSessionExtension.class.getPackage());
        sessionExtensionPoint = (DefaultExtensionPoint<SessionExtensionPoint>) extensionManager.getExtensionByPoint(SessionExtensionPoint.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = JdbcOperationsUtil.getTestConnection();
        String connectionId = sessionExtensionPoint.killQuery(connection, "testConnectionId");
        assertEquals("testConnectionId", connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = JdbcOperationsUtil.getTestConnection();
        String schemaName = "testSchema";
        sessionExtensionPoint.switchSchema(connection, schemaName);
        assertEquals(schemaName, getCurrentSchema(connection));
    }

    @Test
    public void testSwitchSchemaSameSchema() throws SQLException {
        Connection connection = JdbcOperationsUtil.getTestConnection();
        String schemaName = "testSchema";
        sessionExtensionPoint.switchSchema(connection, schemaName);
        assertEquals(schemaName, getCurrentSchema(connection));
        sessionExtensionPoint.switchSchema(connection, schemaName);
        assertEquals(schemaName, getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = JdbcOperationsUtil.getTestConnection();
        String currentSchema = sessionExtensionPoint.getCurrentSchema(connection);
        assertSchemaNotEmpty(currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = JdbcOperationsUtil.getTestConnection();
        String connectionId = sessionExtensionPoint.getConnectionId(connection);
        assertConnectionIdNotEmpty(connectionId);
    }

    @Test
    public void testGetConnectionIdNullConnection() {
        assertThrows(NullPointerException.class, () -> sessionExtensionPoint.getConnectionId(null));
    }

    @AfterEach
    public void tearDown() throws SQLException {
        JdbcOperationsUtil.closeConnection(JdbcOperationsUtil.getTestConnection());
    }

    private String getCurrentSchema(Connection connection) throws SQLException {
        return JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class);
    }

    private void assertSchemaNotEmpty(String schema) {
        Optional.ofNullable(schema).orElseThrow(() -> new AssertionError("Schema is empty"));
    }

    private void assertConnectionIdNotEmpty(String connectionId) {
        Optional.ofNullable(connectionId).orElseThrow(() -> new AssertionError("Connection id is empty"));
    }
}