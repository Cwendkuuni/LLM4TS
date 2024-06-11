import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySqlSessionExtension;

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        obMySqlSessionExtension = new OBMySQLSessionExtension();
        obMySqlSessionExtension.killQuery(connection, "12345");
        // Add assertion here
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        obMySqlSessionExtension = new OBMySQLSessionExtension();
        obMySqlSessionExtension.switchSchema(connection, "test_schema");
        // Add assertion here
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        obMySqlSessionExtension = new OBMySQLSessionExtension();
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("test_schema");
        String currentSchema = obMySqlSessionExtension.getCurrentSchema(connection);
        assertEquals("test_schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        obMySqlSessionExtension = new OBMySQLSessionExtension();
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn("54321");
        String connectionId = obMySqlSessionExtension.getConnectionId(connection);
        assertEquals("54321", connectionId);

        // Test exception handling for OceanBaseConnection
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(9999L);
        String connectionIdOceanBase = obMySqlSessionExtension.getConnectionId(oceanBaseConnection);
        assertEquals("9999", connectionIdOceanBase);
    }
}