import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

public class OBMySQLSessionExtensionTest {
    private JdbcOperations jdbcOperations;
    private OBMySQLSessionExtension extension;
    private Connection connection;

    @Before
    public void setUp() {
        jdbcOperations = mock(JdbcOperations.class);
        extension = new OBMySQLSessionExtension();
        connection = mock(Connection.class);

        extension.setJdbcOperations(jdbcOperations);
    }

    @Test
    public void testKillQuery() throws Exception {
        String connectionId = "12345";
        when(jdbcOperations.execute(connection, "KILL QUERY " + connectionId)).thenReturn(1);
        extension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws Exception {
        String schemaName = "test_schema";
        when(connection.getCatalog()).thenReturn("old_schema");
        extension.switchSchema(connection, schemaName);
        assertEquals("test_schema", connection.getCatalog());
        when(connection.getCatalog()).thenReturn("old_schema");
    }

    @Test
    public void testGetCurrentSchema() {
        String currentSchema = "current_schema";
        when(jdbcOperations.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn(currentSchema);
        assertEquals(currentSchema, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws Exception {
        String connectionId = "12345";
        when(jdbcOperations.queryForObject(connection, "select connection_id()", String.class)).thenReturn(connectionId);
        assertEquals(connectionId, extension.getConnectionId(connection));

        connection.setAutoCommit(false);
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        assertEquals("server_thread_id_123", extension.getConnectionId(connection));
    }
}