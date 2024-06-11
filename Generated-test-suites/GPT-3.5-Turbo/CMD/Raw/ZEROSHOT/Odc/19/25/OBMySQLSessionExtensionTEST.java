import static org.junit.Assert.*;
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
        String connectionId = "123456";

        obMySQLSessionExtension.killQuery(connection, connectionId);

        // Verify that correct SQL query is executed on connection
        verify(connection, times(1)).createStatement();
        verify(connection.createStatement(), times(1)).execute(eq("KILL QUERY 123456"));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        
        // Verify that setCatalog is called on connection with correct schema
        verify(connection, times(1)).setCatalog(eq(schemaName));
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);

        // Mock query response
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn("testSchema");

        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);

        assertEquals("testSchema", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);

        // Mock query response
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn("123456");

        String connectionId = obMySQLSessionExtension.getConnectionId(connection);

        assertEquals("123456", connectionId);
    }

}