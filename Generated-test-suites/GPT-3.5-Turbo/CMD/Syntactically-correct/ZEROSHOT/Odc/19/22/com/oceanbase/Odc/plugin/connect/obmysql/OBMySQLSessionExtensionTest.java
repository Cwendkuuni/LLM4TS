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
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();

        sessionExtension.killQuery(connection, "123");

        // Verify that the execute method was called with the correct query
        // You can use Mockito.verify here if needed
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();

        // Mocking getCurrentSchema method to return "some_schema"
        when(sessionExtension.getCurrentSchema(connection)).thenReturn("some_schema");

        sessionExtension.switchSchema(connection, "new_schema");

        // Verify that the setCatalog method was called with the correct schemaName
        // You can use Mockito.verify here if needed
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();

        // Mocking queryForObject method to return "some_schema"
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn("some_schema");

        String currentSchema = sessionExtension.getCurrentSchema(connection);

        assertEquals("some_schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();

        // Mocking getServerThreadId method to return 123456
        OceanBaseConnection mockOceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(123456L);

        String connectionId = sessionExtension.getConnectionId(connection);

        assertEquals("123456", connectionId);
    }
}