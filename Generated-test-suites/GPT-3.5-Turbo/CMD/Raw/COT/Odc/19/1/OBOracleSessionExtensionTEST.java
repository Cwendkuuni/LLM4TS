import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        // Mocking Connection and connectionId
        Connection connection = mock(Connection.class);
        String connectionId = "123";
        
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        sessionExtension.killQuery(connection, connectionId);
        
        // Verify that execute method was called with the correct query
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() {
        // Mocking Connection and schemaName
        Connection connection = mock(Connection.class);
        String schemaName = "TEST_SCHEMA";
        
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        
        try {
            sessionExtension.switchSchema(connection, schemaName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Verify that update method was called with the correct alterSql query
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).update("ALTER SESSION SET CURRENT_SCHEMA='TEST_SCHEMA'");
    }

    @Test
    public void testGetCurrentSchema() {
        // Mocking Connection
        Connection connection = mock(Connection.class);

        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        
        // Assert that the returned current schema is not null
        assertNotNull(currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        // Mocking Connection
        Connection connection = mock(Connection.class);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(789);
        
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        
        String connectionId = sessionExtension.getConnectionId(connection);
        
        // Assert that the returned connection ID is equal to "789"
        assertEquals("789", connectionId);
    }
}