import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        // Mocking JdbcOperationsUtil
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(connection);
        
        obMySQLSessionExtension.killQuery(connection, connectionId);

        // Verify the execution of KILL QUERY statement
        // Add your verification logic here
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";

        // Mocking getCurrentSchema method
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("default_schema");

        obMySQLSessionExtension.switchSchema(connection, schemaName);

        // Verify the switchSchema logic
        // Add your verification logic here
    }

    @Test
    public void testGetCurrentSchema() {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        Connection connection = mock(Connection.class);
        String expectedSchema = "test_schema";

        // Mocking JdbcOperationsUtil
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(connection);
        when(jdbcOperationsUtil.queryForObject("SELECT DATABASE()", String.class)).thenReturn(expectedSchema);

        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);

        assertEquals(expectedSchema, currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        Connection connection = mock(Connection.class);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        String expectedConnectionId = "54321";

        // Mocking JdbcOperationsUtil
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(connection);
        when(jdbcOperationsUtil.queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException());
        when(oceanBaseConnection.getServerThreadId()).thenReturn(Long.parseLong(expectedConnectionId));
        when(connection instanceof OceanBaseConnection).thenReturn(true);

        String connectionId = obMySQLSessionExtension.getConnectionId(connection);

        assertEquals(expectedConnectionId, connectionId);
    }

}