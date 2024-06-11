import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.pf4j.ExtensionManager;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private OBMySQLSessionExtension obMySQLSessionExtension;
    private SessionExtensionPoint sessionExtensionPoint;
    private ExtensionManager extensionManager;
    private Properties properties;

    @Before
    public void setUp() throws Exception {
        connection = mock(Connection.class);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        sessionExtensionPoint = PowerMockito.mock(SessionExtensionPoint.class);
        extensionManager = PowerMockito.mock(ExtensionManager.class);
        properties = new Properties();
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(JdbcOperationsUtil.class);

        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(sessionExtensionPoint);
        when(extensionManager.getExtensions(SessionExtensionPoint.class)).thenReturn(new Object[]{obMySQLSessionExtension});
        when(sessionExtensionPoint.killQuery(connection, anyString())).thenReturn(null);
        when(sessionExtensionPoint.switchSchema(connection, anyString())).thenReturn(true);
        when(sessionExtensionPoint.getCurrentSchema(connection)).thenReturn("testSchema");
        when(sessionExtensionPoint.getConnectionId(connection)).thenReturn("testConnectionId");
    }

    @After
    public void tearDown() throws Exception {
        PowerMockito.resetAll();
    }

    @Test
    public void testKillQuery() throws SQLException {
        obMySQLSessionExtension.killQuery(connection, "testConnectionId");
        assertNull(connection.createStatement().execute("SELECT COUNT(*) FROM INFORMATION_SCHEMA.PROCESSLIST WHERE ID = 'testConnectionId' AND COMMAND = 'KILL QUERY'"));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        obMySQLSessionExtension.switchSchema(connection, "newSchemaName");
        assertEquals("newSchemaName", JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaToSameSchema() throws SQLException {
        obMySQLSessionExtension.switchSchema(connection, "testSchema");
        obMySQLSessionExtension.switchSchema(connection, "testSchema");
    }

    @Test
    public void testGetCurrentSchema() {
        assertEquals("testSchema", obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        assertEquals("testConnectionId", obMySQLSessionExtension.getConnectionId(connection));
    }
}