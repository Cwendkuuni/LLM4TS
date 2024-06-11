import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.impl.DefaultExtensionPoint;
import org.pf4j.impl. Pf4JClassLoader;
import org.pf4j.impl. Pf4JClassLoaderFactory;
import org.pf4j.impl.util.ClassLoaderUtil;
import org.pf4j.impl.util.PropertiesUtil;
import org.powermock.api.mockito.statically.PowerMockStatic;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.testng.PowerMockModule;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.mockito.Mockito.*;

@PrepareForTest({OBOracleSessionExtension.class})
public class OBOracleSessionExtensionTest {

    private OceanBaseConnection connection;
    private ExtensionManager extensionManager;
    private DefaultExtensionPoint sessionExtensionPoint;

    @Before
    public void setup() throws Exception {
        // Initialize mocks and test environment setup
        connection = mock(OceanBaseConnection.class);
        extensionManager = mock(ExtensionManager.class);
        sessionExtensionPoint = new DefaultExtensionPoint();

        Extension extension = new OBOracleSessionExtension();
        PowerMockStatic.setStatic(OBOracleSessionExtension.class, "getExtensionManager", () -> extensionManager);

        ClassLoader classLoader = new Pf4JClassLoaderFactory().createClassLoader();
        Pf4JClassLoader pf4JClassLoader = new Pf4JClassLoader(classLoader);
        Properties properties = PropertiesUtil.getPropertiesFromClasspath("application.properties");
        extensionManager = new ExtensionManager(pf4JClassLoader, properties);
        extensionManager.start();
        extensionManager.getExtensionPoint(SessionExtensionPoint.class).registerExtension(extension);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "test_connection_id";
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));

        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.killQuery(connection, connectionId);

        verify(connection).createStatement();
        verify(connection.createStatement()).executeUpdate("KILL QUERY " + connectionId);
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() throws SQLException {
        String connectionId = "test_connection_id";
        when(connection.createStatement()).thenThrow(new SQLException("Test Exception"));

        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.killQuery(connection, connectionId);

        verify(connection).createStatement();
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        String currentSchema = "current_schema";

        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connection.createStatement().executeUpdate(anyString())).thenReturn(1);

        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.switchSchema(connection, schemaName);

        verify(connection.createStatement()).executeUpdate("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
        verify(connection).createStatement().executeUpdate(eq("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName));
        verify(connection).createStatement().executeUpdate(anyString());
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        String schemaName = "test_schema";
        String currentSchema = "current_schema";

        when(connection.createStatement()).thenThrow(new SQLException("Test Exception"));

        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.switchSchema(connection, schemaName);

        verify(connection.createStatement()).executeUpdate("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String queryResult = "test_schema";
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connection.createStatement().queryForObject(eq("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL"), eq(String.class))).thenReturn(queryResult);

        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        String result = obOracleSessionExtension.getCurrentSchema(connection);

        Assert.assertEquals(queryResult, result);
    }

    @Test
    public void testGetConnectionIdNull() throws SQLException {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        String result = obOracleSessionExtension.getConnectionId(null);

        Assert.assertNull(result);
    }

    @Test
    public void testGetConnectionIdOceanBaseConnection() throws SQLException {
        String connectionId = "test_connection_id";
        when(connection.getServerThreadId()).thenReturn(connectionId);

        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        String result = obOracleSessionExtension.getConnectionId(connection);

        Assert.assertEquals(connectionId, result);
    }

    @Test
    public void testGetConnectionIdSQLException() throws SQLException {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        String result = obOracleSessionExtension.getConnectionId(connection);

        Assert.assertNull(result);
    }
}