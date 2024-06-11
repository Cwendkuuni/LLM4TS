import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";
        
        // Test case for killQuery method
        obMySQLSessionExtension.killQuery(connection, connectionId);
        
        // Verify that the execute method is called with correct query
        verify(JdbcOperationsUtil.getJdbcOperations(connection), times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        
        // Test case for switchSchema method
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        
        // Verify that the setCatalog method is called with correct schemaName
        verify(connection, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        
        // Test case for getCurrentSchema method
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        
        // Assert that the return value is not null
        assertNotNull(currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        
        // Test case for getConnectionId method
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        
        // Assert that the return value is not null
        assertNotNull(connectionId);
    }
}