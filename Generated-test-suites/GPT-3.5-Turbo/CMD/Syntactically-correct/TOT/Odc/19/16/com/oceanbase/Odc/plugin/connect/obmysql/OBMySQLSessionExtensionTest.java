import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        Connection connection = Mockito.mock(Connection.class);
        String connectionId = "12345";
        
        obMySQLSessionExtension.killQuery(connection, connectionId);
        
        // Verify that the killQuery method was called with the correct connection and query
        Mockito.verify(connection, Mockito.times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        String schemaName = "test_schema";
        
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        
        // Verify that the switchSchema method was called with the correct schema name
        Mockito.verify(connection, Mockito.times(1)).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = Mockito.mock(Connection.class);
        String expectedSchema = "test_schema";
        
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
            .thenReturn(expectedSchema);
        
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        
        assertEquals(expectedSchema, currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        String expectedConnectionId = "54321";
        
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
            .thenReturn(expectedConnectionId);
        
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        
        // Verify that the getConnectionId method returned the correct connection ID
        assertEquals(expectedConnectionId, connectionId);
    }

}