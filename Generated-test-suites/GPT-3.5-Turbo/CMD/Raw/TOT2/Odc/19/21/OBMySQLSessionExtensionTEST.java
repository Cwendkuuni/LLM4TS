import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        // Create mocks
        Connection connection = mock(Connection.class);
        
        // Call the method
        new OBMySQLSessionExtension().killQuery(connection, "12345");
        
        // Verify that the method was called with the correct query
        verify(connection, times(1)).execute("KILL QUERY 12345");
    }
    
    @Test
    public void testSwitchSchema() throws SQLException {
        // Create mocks
        Connection connection = mock(Connection.class);
        
        // Call the method
        new OBMySQLSessionExtension().switchSchema(connection, "test_schema");
        
        // Verify that the method switches the schema correctly
        verify(connection, times(1)).setCatalog("test_schema");
    }
    
    @Test
    public void testGetCurrentSchema() {
        // Create mocks
        Connection connection = mock(Connection.class);
        
        // Call the method
        String currentSchema = new OBMySQLSessionExtension().getCurrentSchema(connection);
        
        // Verify that the method returns the correct schema
        assertEquals("SELECT DATABASE()", currentSchema);
    }
    
    @Test
    public void testGetConnectionId() {
        // Create mocks
        Connection connection = mock(Connection.class);
        OceanBaseConnection obConnection = mock(OceanBaseConnection.class);
        when(obConnection.getServerThreadId()).thenReturn(12345);
        
        // Call the method
        String connectionId = new OBMySQLSessionExtension().getConnectionId(obConnection);
        
        // Verify that the method returns the correct connection id
        assertEquals("12345", connectionId);
    }
    
    @Test(expected = SQLException.class)
    public void testSwitchSchemaException() throws SQLException {
        // Create mocks
        Connection connection = mock(Connection.class);
        doThrow(new SQLException()).when(connection).setCatalog(anyString());
        
        // Call the method
        new OBMySQLSessionExtension().switchSchema(connection, "test_schema_exception");
    }
    
    @Test
    public void testGetConnectionIdException() {
        // Create mocks
        Connection connection = mock(Connection.class);
        
        // Call the method
        String connectionId = new OBMySQLSessionExtension().getConnectionId(connection);
        
        // Verify that the method handles the exception correctly
        assertNull(connectionId);
    }
}