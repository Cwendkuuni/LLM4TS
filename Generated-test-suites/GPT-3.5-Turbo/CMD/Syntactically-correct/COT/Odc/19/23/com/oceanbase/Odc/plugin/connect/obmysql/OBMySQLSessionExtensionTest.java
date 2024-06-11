
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();

    // Test killQuery method
    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "123";
        obMySQLSessionExtension.killQuery(connection, connectionId);
        // No assertion as it is void method, just checking for any exceptions
    }

    // Test switchSchema method
    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        when(connection.getCatalog()).thenReturn("default_schema");
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        assertEquals(schemaName, connection.getCatalog());
    }

    // Test getCurrentSchema method
    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn("test_schema");
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals("test_schema", currentSchema);
    }

    // Test getConnectionId method
    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
                .thenReturn("456");
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals("456", connectionId);
    }
}

