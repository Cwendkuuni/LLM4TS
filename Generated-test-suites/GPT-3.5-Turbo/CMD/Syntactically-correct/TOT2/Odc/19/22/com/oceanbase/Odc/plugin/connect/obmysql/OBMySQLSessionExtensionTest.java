import static org.junit.Assert.assertEquals;
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

        obMySQLSessionExtension.killQuery(connection, connectionId);

        // Verify that the execute method is called with the correct query
        verify(JdbcOperationsUtil.getJdbcOperations(connection), times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";

        obMySQLSessionExtension.switchSchema(connection, schemaName);

        // Verify that the setCatalog method is called with the correct schemaName
        verify(connection, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("test_schema");

        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);

        // Verify that the correct schema name is returned
        assertEquals("test_schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn("1234");

        String connectionId = obMySQLSessionExtension.getConnectionId(connection);

        // Verify that the correct connection id is returned
        assertEquals("1234", connectionId);
    }
}