import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "123";
        
        obMySQLSessionExtension.killQuery(connection, connectionId);
        
        verify(connection).prepareStatement("KILL QUERY 123");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("old_schema");
      
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        
        verify(connection).setCatalog("test_schema");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
            .thenReturn("test_schema");
      
        String result = obMySQLSessionExtension.getCurrentSchema(connection);
        
        assertEquals("test_schema", result);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
            .thenReturn("456");
      
        String result = obMySQLSessionExtension.getConnectionId(connection);
        
        assertEquals("456", result);
    }
}