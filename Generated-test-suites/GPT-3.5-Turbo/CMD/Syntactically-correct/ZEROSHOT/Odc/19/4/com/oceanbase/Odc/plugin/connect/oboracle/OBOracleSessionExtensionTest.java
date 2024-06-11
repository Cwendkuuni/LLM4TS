import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection mockConnection;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        mockConnection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        // Given
        String connectionId = "123";

        // When
        obOracleSessionExtension.killQuery(mockConnection, connectionId);

        // Then
        verify(mockConnection, times(1)).prepareStatement("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        // Given
        String schemaName = "new_schema";

        // When
        obOracleSessionExtension.switchSchema(mockConnection, schemaName);

        // Then
        verify(mockConnection, times(1)).prepareStatement("ALTER SESSION SET CURRENT_SCHEMA='new_schema'");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        // Given
        when(mockConnection.prepareStatement(anyString())).thenReturn(null);
        when(mockConnection.executeQuery(anyString())).thenReturn("test_schema");

        // When
        String currentSchema = obOracleSessionExtension.getCurrentSchema(mockConnection);

        // Then
        assertEquals("test_schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        // Given
        when(mockConnection.prepareStatement(anyString())).thenReturn(null);
        when(mockConnection.executeQuery(anyString())).thenThrow(new SQLException());
        when(mockConnection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) mockConnection).getServerThreadId()).thenReturn(456L);

        // When
        String connectionId = obOracleSessionExtension.getConnectionId(mockConnection);

        // Then
        assertEquals("456", connectionId);
    }
}