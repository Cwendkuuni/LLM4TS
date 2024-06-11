import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;

public class OBMySQLSessionExtensionTest {

    OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "123";
        
       obMySQLSessionExtension.killQuery(connection, connectionId);
        
        // Verify that the execute method was called with the correct query
        verify(connection, times(1)).prepareStatement("KILL QUERY 123");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        
        verify(connection, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);

        obMySQLSessionExtension.getCurrentSchema(connection);

        // Verify that the queryForObject method was called with the correct query and return type
        verify(connection, times(1)).prepareStatement("SELECT DATABASE()");
        verify(connection, times(1)).queryForObject(anyString(), eq(String.class));
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        String expectedConnectionId = "123";

        obMySQLSessionExtension.getConnectionId(connection);

        // Verify that the queryForObject method was called with the correct query and return type
        verify(connection, times(1)).prepareStatement("select connection_id()");
        verify(connection, times(1)).queryForObject(anyString(), eq(String.class));
    }
}