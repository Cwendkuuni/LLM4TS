import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class OBOracleSessionExtensionTest {

    private Connection connection;
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        connection = mock(Connection.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        // Test typical use case
        obOracleSessionExtension.killQuery(connection, "123456");

        // Test edge case
        obOracleSessionExtension.killQuery(connection, "");

        // Test error scenario
        assertThrows(SQLException.class, () -> obOracleSessionExtension.killQuery(connection, null));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        // Test typical use case
        obOracleSessionExtension.switchSchema(connection, "new_schema");

        // Test edge case
        obOracleSessionExtension.switchSchema(connection, "");

        // Test error scenario
        assertThrows(SQLException.class, () -> obOracleSessionExtension.switchSchema(connection, null));
    }

    @Test
    public void testGetCurrentSchema() {
        // Test typical use case
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertNotNull(currentSchema);

        // Test edge case
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn(null);
        assertNull(obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        // Test typical use case
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertNotNull(connectionId);

        // Test edge case
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenThrow(new SQLException());
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        assertNotNull(obOracleSessionExtension.getConnectionId(connection));

        // Test error scenario
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenThrow(new SQLException());
        when(connection instanceof OceanBaseConnection).thenReturn(false);
        assertNull(obOracleSessionExtension.getConnectionId(connection));
    }
}