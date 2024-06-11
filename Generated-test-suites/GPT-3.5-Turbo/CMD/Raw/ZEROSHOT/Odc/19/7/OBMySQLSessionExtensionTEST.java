import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBMySQLSessionExtensionTest {

    OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        obMySQLSessionExtension.killQuery(connection, connectionId);

        // Verify that the execute method was called with the correct query
        // You can add more verification here based on your implementation
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";

        // Mocking the getCurrentSchema method
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("old_schema");

        obMySQLSessionExtension.switchSchema(connection, schemaName);

        // Verify that the setCatalog method was called with the correct schemaName
        // Verify any other behavior based on your implementation
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);

        String schema = "test_schema";

        // Mocking the queryForObject method
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn(schema);

        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);

        assertEquals(schema, currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);

        // Mocking the queryForObject method
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn("12345");

        String connectionId = obMySQLSessionExtension.getConnectionId(connection);

        assertEquals("12345", connectionId);

        // Test case where queryForObject throws an exception
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException());
        String serverThreadId = "98765";
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(Long.parseLong(serverThreadId));

        connectionId = obMySQLSessionExtension.getConnectionId(connection);

        assertEquals(serverThreadId, connectionId);
    }
}