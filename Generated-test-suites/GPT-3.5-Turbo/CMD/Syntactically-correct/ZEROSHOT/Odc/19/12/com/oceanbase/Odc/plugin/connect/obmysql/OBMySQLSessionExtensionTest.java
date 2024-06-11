import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "123";
        
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.killQuery(connection, connectionId);
        
        // Verify the execute method is called with the correct query
        verify(connection).execute("KILL QUERY " + connectionId);
    }
    
    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        
        // Mock the getCurrentSchema method to return a different schema
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("other_schema");
        
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        
        // Verify setCatalog is called with the correct schemaName
        verify(connection).setCatalog(schemaName);
    }
    
    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);

        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();

        // Mock the queryForObject method to return a specific schema name
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
            .thenReturn("test_schema");

        assertEquals("test_schema", obMySQLSessionExtension.getCurrentSchema(connection));
    }
    
    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        
        // Mock the queryForObject method to return a specific connection id
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
            .thenReturn("456");
        
        assertEquals("456", obMySQLSessionExtension.getConnectionId(connection));
    }
}