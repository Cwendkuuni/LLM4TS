import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        obOracleSessionExtension.killQuery(connection, connectionId);

        // Verify that JdbcOperationsUtil.execute was called with the correct SQL query
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";

        // Mocking the getCurrentSchema method
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("other_schema");

        obOracleSessionExtension.switchSchema(connection, schemaName);

        // Verify that JdbcOperationsUtil.update was called with the correct SQL query
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).update("ALTER SESSION SET CURRENT_SCHEMA='test_schema'");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);

        // Mocking the queryForObject method to return a specific schema
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn("test_schema");

        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);

        assertEquals("test_schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);

        // Mocking the queryForObject method to return a specific connection id
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn("54321");

        String connectionId = obOracleSessionExtension.getConnectionId(connection);

        assertEquals("54321", connectionId);
    }
}