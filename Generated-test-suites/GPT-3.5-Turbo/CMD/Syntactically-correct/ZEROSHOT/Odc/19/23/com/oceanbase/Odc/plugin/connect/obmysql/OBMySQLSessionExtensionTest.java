import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension;
    private Connection connection;

    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        connection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "123";
        obMySQLSessionExtension.killQuery(connection, connectionId);
        // Verify the execution of the query
        // Add verification logic here
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        // Mock the behavior of getCurrentSchema method
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("old_schema");

        obMySQLSessionExtension.switchSchema(connection, schemaName);
        // Verify the behavior of switchSchema method
        // Add verification logic here
    }

    @Test
    public void testGetCurrentSchema() {
        // Mock the query result for getCurrentSchema method
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("test_schema");

        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals("test_schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        // Mock the query result for getConnectionId method
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn("789");

        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals("789", connectionId);
    }
}