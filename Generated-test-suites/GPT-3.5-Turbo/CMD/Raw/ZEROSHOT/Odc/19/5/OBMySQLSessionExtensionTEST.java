import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    Connection connectionMock;

    OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();

    @Test
    public void testKillQuery() {
        obMySQLSessionExtension.killQuery(connectionMock, "1");
        verify(JdbcOperationsUtil.getJdbcOperations(connectionMock), times(1)).execute("KILL QUERY 1");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(obMySQLSessionExtension.getCurrentSchema(connectionMock)).thenReturn("schema1");
        obMySQLSessionExtension.switchSchema(connectionMock, "schema1");
        verify(connectionMock, never()).setCatalog(anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        when(JdbcOperationsUtil.getJdbcOperations(connectionMock).queryForObject(anyString(), eq(String.class))).thenReturn("schema1");
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connectionMock);
        assertEquals("schema1", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        when(JdbcOperationsUtil.getJdbcOperations(connectionMock).queryForObject(anyString(), eq(String.class))).thenReturn("1");
        String connectionId = obMySQLSessionExtension.getConnectionId(connectionMock);
        assertEquals("1", connectionId);

        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(2L);
        when(connectionMock instanceof OceanBaseConnection).thenReturn(true);
        String connectionIdFromConnection = obMySQLSessionExtension.getConnectionId(connectionMock);
        assertEquals("2", connectionIdFromConnection);
    }
}