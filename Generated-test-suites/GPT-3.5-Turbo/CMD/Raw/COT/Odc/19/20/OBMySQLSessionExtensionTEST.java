import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension mySqlSessionExtension;

    @Before
    public void setUp() {
        mySqlSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        mySqlSessionExtension.killQuery(connection, connectionId);

        // Verify that execute method is called with expected query
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";

        mySqlSessionExtension.switchSchema(connection, schemaName);

        // Verify that setCatalog method is called with expected schema name
        verify(connection).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        String expectedSchemaName = "test_schema";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn(expectedSchemaName);

        String actualSchemaName = mySqlSessionExtension.getCurrentSchema(connection);

        assertEquals(expectedSchemaName, actualSchemaName);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        String expectedConnectionId = "54321";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn(expectedConnectionId);

        String actualConnectionId = mySqlSessionExtension.getConnectionId(connection);

        assertEquals(expectedConnectionId, actualConnectionId);
    }

    @Test
    public void testGetConnectionIdWithException() {
        Connection connection = mock(OceanBaseConnection.class);
        String expectedConnectionId = "12345";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenThrow(SQLException.class);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(12345);

        String actualConnectionId = mySqlSessionExtension.getConnectionId(connection);

        assertEquals(expectedConnectionId, actualConnectionId);
    }
}