import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension sessionExtension;

    @Before
    public void setUp() {
        sessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        sessionExtension.killQuery(connection, connectionId);

        // Verify that the correct SQL query is executed with the connection
        verify(connection, times(1)).prepareStatement(eq("KILL QUERY 12345"));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";

        // Test case where the current schema is different from the provided schema
        when(sessionExtension.getCurrentSchema(connection)).thenReturn("other_schema");
        sessionExtension.switchSchema(connection, schemaName);
        verify(connection, times(1)).setCatalog(eq("test_schema"));

        // Test case where the current schema is the same as the provided schema
        when(sessionExtension.getCurrentSchema(connection)).thenReturn("test_schema");
        sessionExtension.switchSchema(connection, schemaName);
        verify(connection, never()).setCatalog(anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);

        // Test case when querying the current schema returns a valid schema name
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(eq("SELECT DATABASE()"), eq(String.class)))
                .thenReturn("test_schema");
        assertEquals("test_schema", sessionExtension.getCurrentSchema(connection));

        // Test case when querying the current schema returns null   
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(eq("SELECT DATABASE()"), eq(String.class)))
                .thenReturn(null);
        assertNull(sessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);

        // Test case when connection is not an instance of OceanBaseConnection
        assertNull(sessionExtension.getConnectionId(connection));

        // Test case when connection is an instance of OceanBaseConnection
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(12345L);
        assertEquals("12345", sessionExtension.getConnectionId(oceanBaseConnection));
    }
}