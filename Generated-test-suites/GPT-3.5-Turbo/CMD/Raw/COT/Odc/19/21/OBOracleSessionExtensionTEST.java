import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBOracleSessionExtensionTest {

    OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";
        
        sessionExtension.killQuery(connection, connectionId);
        
        // Verify that the execute method is called with the correct query
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        
        // Test when current schema is not equal to the given schema name
        when(sessionExtension.getCurrentSchema(connection)).thenReturn("defaultSchema");
        sessionExtension.switchSchema(connection, schemaName);
        
        // Verify that the update method is called with the correct SQL query
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).update("ALTER SESSION SET CURRENT_SCHEMA='testSchema'");
        
        // Test when current schema is equal to the given schema name
        when(sessionExtension.getCurrentSchema(connection)).thenReturn("testSchema");
        sessionExtension.switchSchema(connection, schemaName);
        
        // Verify that the update method is not called again
        verify(JdbcOperationsUtil.getJdbcOperations(connection), never()).update(anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        
        // Mocking the query result to return "testSchema"
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn("testSchema");
        
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        
        assertEquals("testSchema", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        
        // Mocking the query result to throw an exception
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenThrow(new SQLException());
        
        // Mocking OceanBaseConnection behavior
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(12345L);
        
        // Ensure proper connection ID retrieval in case of exception
        String connectionId = sessionExtension.getConnectionId(oceanBaseConnection);
        assertEquals("12345", connectionId);
        
        // Ensure proper connection ID retrieval when query is successful
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn("67890");
        
        connectionId = sessionExtension.getConnectionId(connection);
        assertEquals("67890", connectionId);
    }
}