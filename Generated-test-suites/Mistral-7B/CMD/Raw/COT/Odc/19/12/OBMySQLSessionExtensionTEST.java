import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;
import org.pf4j.impl.DefaultExtensionManager;
import org.pf4j.impl.DefaultExtensionPoint;
import org.pf4j.impl.DefaultExtensionWrapper;
import org.pf4j.impl.DefaultPluginWrapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension extension;
    private Connection connection;
    private DefaultExtensionManager extensionManager;

    @Before
    public void setUp() throws Exception {
        extension = new OBMySQLSessionExtension();
        extensionManager = new DefaultExtensionManager();
        extensionManager.init(new PluginWrapper("com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension", "1.0.0", "OceanBase MySQL Session Extension"));
        extensionManager.loadExtension(extension);
        connection = DriverManager.getConnection("jdbc:obmysql://localhost:27017/test", "root", "password");
    }

    @Test
    public void testKillQuery() throws SQLException {
        extension.killQuery(connection, "1");
        assertThrows(SQLException.class, () -> extension.killQuery(connection, ""));
        assertThrows(SQLException.class, () -> extension.killQuery(connection, null));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        extension.switchSchema(connection, "test");
        assertEquals("test", JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class));
        assertThrows(SQLException.class, () -> extension.switchSchema(connection, ""));
        assertThrows(SQLException.class, () -> extension.switchSchema(connection, null));
        assertThrows(SQLException.class, () -> extension.switchSchema(connection, "non_existent_schema"));

        connection.setCatalog("test2");
        extension.switchSchema(connection, "test");
        assertEquals("test", JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class));
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = extension.getCurrentSchema(connection);
        assertNotNull(currentSchema);
        assertEquals("test", extension.getCurrentSchema(connection)); // test after switching schema
        assertThrows(SQLException.class, () -> extension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = extension.getConnectionId(connection);
        assertNotNull(connectionId);
        Optional<String> connectionIdOptional = Optional.ofNullable(connectionId);
        assertEquals(connectionId, connectionIdOptional.orElse(""));
        assertThrows(SQLException.class, () -> extension.getConnectionId(null));
    }
}