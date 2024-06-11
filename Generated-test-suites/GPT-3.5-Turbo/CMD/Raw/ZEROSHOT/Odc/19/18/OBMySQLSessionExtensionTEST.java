import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
        
        verify(connection).prepareStatement("KILL QUERY 123");
    }
    
    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        
        verify(connection).setCatalog(schemaName);
    }
    
    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        String expectedSchema = "testSchema";
        
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
            .thenReturn(expectedSchema);
        
        assertEquals(expectedSchema, obMySQLSessionExtension.getCurrentSchema(connection));
    }
    
    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        String expectedConnectionId = "12345";
        
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
            .thenReturn(expectedConnectionId);
        
        assertEquals(expectedConnectionId, obMySQLSessionExtension.getConnectionId(connection));
    }

}