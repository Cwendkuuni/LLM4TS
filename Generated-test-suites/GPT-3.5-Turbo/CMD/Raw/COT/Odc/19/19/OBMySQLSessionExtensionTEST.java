import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;

public class OBMySQLSessionExtensionTest {
    OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
    
    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "123";
        extension.killQuery(connection, connectionId);
        // Check if the query is executed correctly without exceptions
        // No return value to check here
    }
    
    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        extension.switchSchema(connection, schemaName);
        // Verify that the schema has been successfully switched
        assertEquals(schemaName, connection.getCatalog());
    }
    
    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        String expectedSchema = "defaultSchema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn(expectedSchema);
        String actualSchema = extension.getCurrentSchema(connection);
        assertEquals(expectedSchema, actualSchema);
    }
    
    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        String expectedConnectionId = "456";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
                .thenReturn(expectedConnectionId);
        String actualConnectionId = extension.getConnectionId(connection);
        assertEquals(expectedConnectionId, actualConnectionId);
    }
    
    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        String expectedThreadId = "789";
        when(oceanBaseConnection.getServerThreadId()).thenReturn(Long.valueOf(expectedThreadId));
        String actualConnectionId = extension.getConnectionId(oceanBaseConnection);
        assertEquals(expectedThreadId, actualConnectionId);
    }
}