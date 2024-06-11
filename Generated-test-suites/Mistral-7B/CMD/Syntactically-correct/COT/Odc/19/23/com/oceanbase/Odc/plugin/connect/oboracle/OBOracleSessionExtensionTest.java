import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.impl.DefaultExtensionManager;
import org.pf4j.impl.DefaultExtensionPoint;
import org.pf4j.impl.layered.LayeredExtensionManager;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({OBOracleSessionExtension.class, JdbcOperationsUtil.class})
public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private DefaultExtensionManager extensionManager;
    private Connection connection;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Properties properties = new Properties();
        extensionManager = new DefaultExtensionManager();
        extensionManager = new LayeredExtensionManager(extensionManager);
        obOracleSessionExtension = new OBOracleSessionExtension();
        extensionManager.registerExtensionPoint(SessionExtensionPoint.class, obOracleSessionExtension);
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:ora1", "user", "password");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Test killQuery method
    @Test
    public void testKillQuery() {
        obOracleSessionExtension.killQuery(connection, "123");
        verify(connection).createStatement().executeUpdate("KILL QUERY 123");
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithNullConnection() {
        obOracleSessionExtension.killQuery(null, "123");
        assertThrows(SQLException.class, () -> obOracleSessionExtension.killQuery(null, "123"));
    }

    @Test(expected = NullPointerException.class)
    public void testKillQueryWithNullConnectionId() {
        obOracleSessionExtension.killQuery(connection, null);
        assertThrows(NullPointerException.class, () -> obOracleSessionExtension.killQuery(connection, null));
    }

    // Test switchSchema method
    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        obOracleSessionExtension.switchSchema(connection, schemaName);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        verify(connection).createStatement().executeUpdate("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
        assertEquals(schemaName, currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithNullConnection() {
        obOracleSessionExtension.switchSchema(null, "testSchema");
        assertThrows(SQLException.class, () -> obOracleSessionExtension.switchSchema(null, "testSchema"));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithNullSchemaName() {
        obOracleSessionExtension.switchSchema(connection, null);
        assertThrows(SQLException.class, () -> obOracleSessionExtension.switchSchema(connection, null));
    }

    // Test getCurrentSchema method
    @Test
    public void testgetCurrentSchema() throws SQLException {
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        String schemaName = JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class);
        verify(connection).createStatement().executeQuery(querySql);
        assertEquals(schemaName, currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testgetCurrentSchemaWithNullConnection() {
        String currentSchema = obOracleSessionExtension.getCurrentSchema(null);
        assertThrows(SQLException.class, () -> obOracleSessionExtension.getCurrentSchema(null));
    }

    // Test getConnectionId method
    @Test
    public void testGetConnectionIdWithConnection() throws SQLException {
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        String querySql = "select userenv('sessionid') from dual";
        String id = JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class);
        verify(connection).createStatement().executeQuery(querySql);
        assertEquals(id, connectionId);
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        String connectionId = obOracleSessionExtension.getConnectionId(PowerMockito.mock(OceanBaseConnection.class));
        verifyNoMoreInteractions(connection);
        assertEquals("serverThreadId", connectionId);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionIdWithNullConnection() {
        String connectionId = obOracleSessionExtension.getConnectionId(null);
        assertThrows(SQLException.class, () -> obOracleSessionExtension.getConnectionId(null));
    }
}