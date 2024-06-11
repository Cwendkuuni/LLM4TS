import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionManager;
import org.pf4j.impl.DefaultExtensionManager;
import org.pf4j.impl.impl.DefaultExtensionPoint;
import org.pf4j.impl.impl.DefaultExtensionPointFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBOracleSessionExtensionTest {

    private ExtensionManager extensionManager;
    private SessionExtensionPoint obOracleSessionExtension;
    private Connection connectionMock;

    @Before
    public void setup() {
        extensionManager = new DefaultExtensionManager();
        obOracleSessionExtension = new OBOracleSessionExtension();
        extensionManager.addExtension(obOracleSessionExtension);
        connectionMock = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "12345";
        when(connectionMock.createStatement()).thenReturn(mock(java.sql.Statement.class));
        obOracleSessionExtension.killQuery(connectionMock, connectionId);
        assertEquals("KILL QUERY 12345", JdbcOperationsUtil.getJdbcOperations(connectionMock).getLastExecutedStatement(), "Last executed statement should be 'KILL QUERY 12345'");
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() throws SQLException {
        String connectionId = "12345";
        when(connectionMock.createStatement()).thenThrow(new SQLException("Test exception"));
        obOracleSessionExtension.killQuery(connectionMock, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        String currentSchema = "current_schema";
        when(connectionMock.createStatement()).thenReturn(mock(java.sql.Statement.class));
        obOracleSessionExtension.switchSchema(connectionMock, schemaName);
        String actualSchema = obOracleSessionExtension.getCurrentSchema(connectionMock);
        assertEquals(schemaName, actualSchema, "Switched schema to " + schemaName);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        String schemaName = "test_schema";
        when(connectionMock.createStatement()).thenThrow(new SQLException("Test exception"));
        obOracleSessionExtension.switchSchema(connectionMock, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = "current_schema";
        when(connectionMock.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connectionMock.createStatement().executeQuery(anyString())).thenReturn(mock(java.sql.ResultSet.class));
        when(connectionMock.createStatement().executeQuery(anyString()).next()).thenReturn(true);
        when(connectionMock.createStatement().executeQuery(anyString()).getString("CURRENT_SCHEMA")).thenReturn(currentSchema);
        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connectionMock), "Current schema should be " + currentSchema);
    }

    @Test
    public void testGetCurrentSchemaWithNullResultSet() throws SQLException {
        when(connectionMock.createStatement().executeQuery(anyString())).thenReturn(null);
        assertEquals(null, obOracleSessionExtension.getCurrentSchema(connectionMock), "Current schema should be null");
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "12345";
        when(connectionMock.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connectionMock.createStatement().executeQuery(anyString())).thenReturn(mock(java.sql.ResultSet.class));
        when(connectionMock.createStatement().executeQuery(anyString()).next()).thenReturn(true);
        when(connectionMock.createStatement().executeQuery(anyString()).getString("SESSIONID")).thenReturn(connectionId);
        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connectionMock), "Connection id should be " + connectionId);
    }

    @Test
    public void testGetConnectionIdWithEmptyResultSet() throws SQLException {
        when(connectionMock.createStatement().executeQuery(anyString())).thenReturn(mock(java.sql.ResultSet.class));
        when(connectionMock.createStatement().executeQuery(anyString()).next()).thenReturn(false);
        assertEquals(null, obOracleSessionExtension.getConnectionId(connectionMock), "Connection id should be null");
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        String connectionId = "12345";
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(Long.parseLong(connectionId));
        when(connectionMock.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connectionMock), "Connection id should be " + connectionId);
    }
}