import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;
import org.pf4j.Extension;
import org.pf4j.PluginManager;
import org.pf4j.loader.ClassPathPluginLoader;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

@Extension
@Slf4j
public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension;
    private PluginManager pluginManager;
    private Connection connectionMock;

    @BeforeAll
    public static void init() {
        // Initialize the test environment
    }

    @BeforeEach
    public void setUp() throws Exception {
        pluginManager = new PluginManager();
        ClassPathPluginLoader pluginLoader = new ClassPathPluginLoader(OBMySQLSessionExtension.class.getClassLoader(), "com.oceanbase.odc.plugin.connect.obmysql");
        pluginManager.loadPlugin(pluginLoader.getPluginInfos()[0]);
        obMySQLSessionExtension = (OBMySQLSessionExtension) pluginManager.getComponent(OBMySQLSessionExtension.class);
        connectionMock = Mockito.mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws Exception {
        obMySQLSessionExtension.killQuery(connectionMock, "testConnectionId");
        Mockito.verify(connectionMock).createStatement();
        Mockito.verify(connectionMock, Mockito.times(1)).execute("KILL QUERY testConnectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        obMySQLSessionExtension.switchSchema(connectionMock, "testSchemaName");
        Mockito.verify(connectionMock).setCatalog("testSchemaName");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connectionMock);
        Mockito.verify(connectionMock).createStatement();
        Mockito.verify(connectionMock, Mockito.times(1)).queryForObject("SELECT DATABASE()", String.class);
        Assertions.assertEquals("testSchemaName", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = obMySQLSessionExtension.getConnectionId(connectionMock);
        Mockito.verify(connectionMock).createStatement();
        if (connectionMock instanceof OceanBaseConnection) {
            Mockito.verify(connectionMock).getServerThreadId();
        }
        Assertions.assertEquals("testConnectionId", connectionId);
    }

    @Test
    public void testKillQueryWithNullConnection() throws Exception {
        obMySQLSessionExtension.killQuery(null, "testConnectionId");
        Mockito.verifyNoInteractions(connectionMock);
    }

    @Test
    public void testKillQueryWithEmptyConnectionId() throws Exception {
        obMySQLSessionExtension.killQuery(connectionMock, "");
        Mockito.verify(connectionMock).createStatement();
        Mockito.verifyNoInteractions(connectionMock, Mockito.atLeast(1)).execute("KILL QUERY testConnectionId");
    }

    @Test
    public void testKillQueryWithConnectionThatDoesNotSupportKillQuery() throws Exception {
        Mockito.when(connectionMock.createStatement()).thenThrow(new SQLException("Error: Test connection does not support KILL QUERY statement"));
        obMySQLSessionExtension.killQuery(connectionMock, "testConnectionId");
        Mockito.verifyNoInteractions(connectionMock, Mockito.atLeast(1)).execute("KILL QUERY testConnectionId");
    }

    @Test
    public void testSwitchSchemaWithNullSchemaName() throws SQLException {
        obMySQLSessionExtension.switchSchema(connectionMock, null);
        Mockito.verifyNoInteractions(connectionMock);
    }

    @Test
    public void testSwitchSchemaWithEmptySchemaName() throws SQLException {
        obMySQLSessionExtension.switchSchema(connectionMock, "");
        Mockito.verifyNoInteractions(connectionMock);
    }

    @Test
    public void testSwitchSchemaWithConnectionThatFailsToChangeSchema() throws SQLException {
        Mockito.when(connectionMock.setCatalog("testSchemaName")).thenThrow(new SQLException("Error: Failed to change schema"));
        obMySQLSessionExtension.switchSchema(connectionMock, "testSchemaName");
        Mockito.verifyNoInteractions(connectionMock, Mockito.atLeast(1)).setCatalog("testSchemaName");
    }

    @Test
    public void testGetCurrentSchemaWithConnectionThatDoesNotSupportSelectDatabase() throws SQLException {
        Mockito.when(connectionMock.createStatement()).thenThrow(new SQLException("Error: Test connection does not support SELECT DATABASE() statement"));
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connectionMock);
        Mockito.verifyNoInteractions(connectionMock, Mockito.atLeast(1)).queryForObject("SELECT DATABASE()", String.class);
        Assertions.assertNull(currentSchema);
    }

    @AfterEach
    public void tearDown(ExtensionContext context) {
        // Release the test environment
    }

    @AfterAll
    public static void cleanup() {
        // Cleanup after all tests are executed
    }

}