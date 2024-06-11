import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension sessionExtension;
    private Connection connection;

    @Before
    public void setUp() {
        sessionExtension = new OBMySQLSessionExtension();
        connection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        // Mocking the JdbcOperationsUtil class
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);

        sessionExtension.killQuery(connection, "123");

        // Verify that the execute method is called with the correct query
        verify(jdbcOperationsUtil).execute("KILL QUERY 123");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        // Test when current schema is different from the target schema
        when(connection.getCatalog()).thenReturn("current_schema");
        sessionExtension.switchSchema(connection, "target_schema");
        verify(connection).setCatalog("target_schema");

        // Test when current schema is the same as the target schema
        sessionExtension.switchSchema(connection, "current_schema");
        verify(connection, never()).setCatalog(anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        // Mocking the JdbcOperationsUtil class
        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(jdbcOperationsUtil.queryForObject(anyString(), eq(String.class))).thenReturn("test_schema");

        String currentSchema = sessionExtension.getCurrentSchema(connection);

        assertEquals("test_schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        // Test when connection is not an instance of OceanBaseConnection
        String connectionId = sessionExtension.getConnectionId(connection);
        assertNull(connectionId);

        // Test when connection is an instance of OceanBaseConnection
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(12345L);
        connection = oceanBaseConnection;

        connectionId = sessionExtension.getConnectionId(connection);

        assertEquals("12345", connectionId);
    }
}