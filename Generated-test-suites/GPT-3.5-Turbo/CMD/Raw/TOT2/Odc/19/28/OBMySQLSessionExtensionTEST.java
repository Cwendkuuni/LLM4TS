import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

import org.junit.Before;
import org.junit.Test;

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

        verify(JdbcOperationsUtil.getJdbcOperations(connection), times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";

        when(sessionExtension.getCurrentSchema(connection)).thenReturn("oldSchema");
        sessionExtension.switchSchema(connection, schemaName);

        verify(connection, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        String expectedSchema = "testSchema";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn(expectedSchema);

        String currentSchema = sessionExtension.getCurrentSchema(connection);

        assertEquals(expectedSchema, currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(OceanBaseConnection.class);
        String expectedConnectionId = "67890";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
                .thenReturn(expectedConnectionId);

        String connectionId = sessionExtension.getConnectionId(connection);

        assertEquals(expectedConnectionId, connectionId);
    }

    @Test
    public void testGetConnectionIdWithException() throws SQLException {
        Connection connection = mock(OceanBaseConnection.class);
        String expectedConnectionId = "12345";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
                .thenThrow(new SQLException());
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(12345);

        String connectionId = sessionExtension.getConnectionId(connection);

        assertEquals(expectedConnectionId, connectionId);
    }
}