import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connection;

    @Mock
    private OceanBaseConnection oceanBaseConnection;

    @Mock
    private JdbcOperationsUtil jdbcOperationsUtil;

    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        obMySQLSessionExtension.killQuery(connection, "test");

        verify(connection).execute("KILL QUERY test");
    }

    @Test
    public void testSwitchSchema_sameSchema() throws SQLException {
        when(connection.getCatalog()).thenReturn("test");

        obMySQLSessionExtension.switchSchema(connection, "test");

        verify(connection, never()).setCatalog(anyString());
    }

    @Test
    public void testSwitchSchema_differentSchema() throws SQLException {
        when(connection.getCatalog()).thenReturn("test1");

        obMySQLSessionExtension.switchSchema(connection, "test2");

        verify(connection).setCatalog("test2");
    }

    @Test
    public void testGetCurrentSchema() {
        when(connection.queryForObject("SELECT DATABASE()", String.class)).thenReturn("test");

        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);

        assertEquals("test", currentSchema);
    }

    @Test
    public void testGetConnectionId_nonOceanBaseConnection() {
        String expectedId = "test";
        when(connection.queryForObject("select connection_id()", String.class)).thenReturn(expectedId);

        String connectionId = obMySQLSessionExtension.getConnectionId(connection);

        assertEquals(expectedId, connectionId);
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() {
        String expectedId = "12345";
        when(oceanBaseConnection.getServerThreadId()).thenReturn(Long.valueOf(expectedId));
        when(connection.queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException());

        String connectionId = obMySQLSessionExtension.getConnectionId(oceanBaseConnection);

        assertEquals(expectedId, connectionId);
    }
}