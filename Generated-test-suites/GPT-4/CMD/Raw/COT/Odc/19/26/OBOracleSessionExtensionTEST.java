
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import java.sql.Connection;
import org.junit.Before;
import org.junit.Test;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

public class OBOracleSessionExtensionTest {
    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection mockConnection;

    @Before
    public void init() {
        mockConnection = mock(Connection.class, Mockito.RETURNS_DEEP_STUBS);
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        obOracleSessionExtension.killQuery(mockConnection, "123");
        verify(mockConnection, times(1)).execute("KILL QUERY 123");
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithEmptySchema() throws SQLException {
        obOracleSessionExtension.switchSchema(mockConnection, "");
    }

    @Test
    public void testGetCurrentSchema() {
        when(mockConnection.queryForObject(anyString(), eq(String.class))).thenReturn("test");
        String schema = obOracleSessionExtension.getCurrentSchema(mockConnection);
        verify(mockConnection, times(1)).queryForObject(anyString(), eq(String.class));
        assertEquals("test", schema);
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() {
        Connection obConnection = mock(OceanBaseConnection.class);
        when(obConnection.queryForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException());
        when(((OceanBaseConnection) obConnection).getServerThreadId()).thenReturn(123L);
        String connectionId = obOracleSessionExtension.getConnectionId(obConnection);
        assertEquals("123", connectionId);
    }

    @Test(expected = RuntimeException.class)
    public void testGetConnectionIdWithFailure() {
        when(mockConnection.queryForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException());
        obOracleSessionExtension.getConnectionId(mockConnection);
    }
}
