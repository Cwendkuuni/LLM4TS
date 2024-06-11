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
        String connectionId = "123";
      
        obMySQLSessionExtension.killQuery(connection, connectionId);
        // Verify that the execute method was called with the correct query
        verify(connection, times(1)).prepareStatement("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        
        // Test when the current schema is equal to the new schema name
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        
        // Verify that setCatalog method was not called
        verify(connection, never()).setCatalog(anyString());
        
        // Test when the current schema is different from the new schema name
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("another_schema");
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        // Verify that setCatalog method was called with the new schema name
        verify(connection, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        
        // Mock the query result
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("test_schema");
        
        assertEquals("test_schema", obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        String expectedConnectionId = "456";
        
        // Test when queryForObject returns connectionId
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn(expectedConnectionId);
        
        assertEquals(expectedConnectionId, obMySQLSessionExtension.getConnectionId(connection));
        
        // Test when queryForObject throws an exception
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException());
        // Test when the connection is an instanceof OceanBaseConnection
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(999);
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        
        assertEquals("999", obMySQLSessionExtension.getConnectionId(oceanBaseConnection));
    }
}