import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionManager;
import org.pf4j.impl.DefaultExtensionManager;
import org.pf4j.impl.DefaultExtensionPoint;
import org.pf4j.impl.DefaultExtensionPointRegistry;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    private static final String TEST_CONNECTION_ID = "test_connection_id";
    private static final String TEST_SCHEMA_NAME = "test_schema_name";
    private ExtensionManager extensionManager;
    private DefaultExtensionPointRegistry extensionPointRegistry;
    private SessionExtensionPoint obOracleSessionExtension;
    private Connection connectionMock;

    @Before
    public void setup() {
        extensionManager = new DefaultExtensionManager();
        extensionPointRegistry = new DefaultExtensionPointRegistry();
        extensionPointRegistry.register(SessionExtensionPoint.class, OBOracleSessionExtension.class);
        extensionManager.setExtensionPoints(extensionPointRegistry);
        obOracleSessionExtension = (SessionExtensionPoint) extensionManager.getExtension(SessionExtensionPoint.class);
        connectionMock = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        when(connectionMock.isValid()).thenReturn(true);
        obOracleSessionExtension.killQuery(connectionMock, TEST_CONNECTION_ID);
        verify(connectionMock).createStatement();
        verify(connectionMock.createStatement()).execute("KILL QUERY " + TEST_CONNECTION_ID);
    }

    @Test(expected = SQLException.class)
    public void testKillQueryInvalidConnection() throws SQLException {
        when(connectionMock.isValid()).thenReturn(false);
        obOracleSessionExtension.killQuery(connectionMock, TEST_CONNECTION_ID);
        verify(connectionMock).createStatement();
        verify(connectionMock.createStatement()).execute("KILL QUERY " + TEST_CONNECTION_ID);
        verify(connectionMock).close();
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(connectionMock.isValid()).thenReturn(true);
        Properties connectionProperties = new Properties();
        connectionProperties.setProperty("user", "test_user");
        when(connectionMock.getMetaData()).thenReturn(connectionProperties);
        obOracleSessionExtension.switchSchema(connectionMock, TEST_SCHEMA_NAME);
        verify(connectionMock).createStatement();
        String currentSchema = getCurrentSchema(connectionMock);
        String expectedSchema = "test_user" + "_" + TEST_SCHEMA_NAME;
        assertEquals(expectedSchema, currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaInvalidConnection() throws SQLException {
        when(connectionMock.isValid()).thenReturn(false);
        obOracleSessionExtension.switchSchema(connectionMock, TEST_SCHEMA_NAME);
        verify(connectionMock).createStatement();
        verify(connectionMock).close();
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        when(connectionMock.isValid()).thenReturn(true);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connectionMock);
        String expectedSchema = "test_schema_name";
        assertEquals(expectedSchema, currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testGetCurrentSchemaInvalidConnection() throws SQLException {
        when(connectionMock.isValid()).thenReturn(false);
        obOracleSessionExtension.getCurrentSchema(connectionMock);
        verify(connectionMock).close();
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(connectionMock.isValid()).thenReturn(true);
        String connectionId = obOracleSessionExtension.getConnectionId(connectionMock);
        assertEquals(TEST_CONNECTION_ID, connectionId);
    }

    @Test
    public void testGetConnectionIdOceanBaseConnection() throws SQLException {
        when(connectionMock.isValid()).thenReturn(true);
        when(connectionMock.getClass()).thenReturn(OceanBaseConnection.class);
        String connectionId = obOracleSessionExtension.getConnectionId(connectionMock);
        String expectedThreadId = "test_thread_id";
        assertEquals(expectedThreadId, connectionId);
    }

    @Test
    public void testGetConnectionIdInvalidConnection() throws SQLException {
        when(connectionMock.isValid()).thenReturn(false);
        obOracleSessionExtension.getConnectionId(connectionMock);
        verify(connectionMock).close();
    }

    private String getCurrentSchema(Connection connection) throws SQLException {
        return obOracleSessionExtension.getCurrentSchema(connection);
    }
}