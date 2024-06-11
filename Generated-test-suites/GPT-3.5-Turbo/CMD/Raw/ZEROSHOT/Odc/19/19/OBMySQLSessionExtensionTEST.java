import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

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
        // Verify that the execute method is called with the correct query
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        // Verify that the setCatalog method is called with the correct schemaName
        verify(connection).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "default_schema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn(expectedSchema);
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        // Verify that the correct schema is returned
        assertEquals(expectedSchema, currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String expectedConnectionId = "456";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn(expectedConnectionId);
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        // Verify that the correct connectionId is returned
        assertEquals(expectedConnectionId, connectionId);
    }

    @Test
    public void testGetConnectionIdWithException() throws SQLException {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenThrow(new SQLException());
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(789);
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        // Verify that the correct connectionId is returned
        assertEquals("789", connectionId);
    }
}