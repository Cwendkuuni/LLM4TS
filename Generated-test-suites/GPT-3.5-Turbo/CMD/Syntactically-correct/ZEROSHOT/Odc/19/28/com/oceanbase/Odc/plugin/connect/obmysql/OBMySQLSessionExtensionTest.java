import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
        String connectionId = "12345";

        obMySQLSessionExtension.killQuery(connection, connectionId);

        // Verify that execute method is called with the correct SQL query
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";

        obMySQLSessionExtension.switchSchema(connection, schemaName);

        // Verify that setCatalog method is called with the correct schemaName
        verify(connection).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        String queryResult = "test_schema";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(eq("SELECT DATABASE()"), eq(String.class))).thenReturn(queryResult);

        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);

        assertNotNull(currentSchema);
        assertEquals("test_schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        String queryResult = "12345";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(eq("select connection_id()"), eq(String.class))).thenReturn(queryResult);

        String connectionId = obMySQLSessionExtension.getConnectionId(connection);

        assertNotNull(connectionId);
        assertEquals("12345", connectionId);
    }
}