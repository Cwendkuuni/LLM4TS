import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.sql.Connection;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection mockConnection;

    private OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();

    @Test
    public void testKillQuery() {
        String connectionId = "connection_id";
        sessionExtension.killQuery(mockConnection, connectionId);
        verify(mockConnection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "schema_name";
        when(mockConnection.getCatalog()).thenReturn(schemaName, "other_schema");
        sessionExtension.switchSchema(mockConnection, schemaName);
        verify(mockConnection, times(0)).setCatalog(schemaName);

        sessionExtension.switchSchema(mockConnection, "other_schema");
        verify(mockConnection, times(1)).setCatalog("other_schema");
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "current_schema";
        when(mockConnection.queryForObject(anyString(), eq(String.class))).thenReturn(expectedSchema);
        String actualSchema = sessionExtension.getCurrentSchema(mockConnection);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String expectedConnectionId = "connection_id";
        when(mockConnection.queryForObject(anyString(), eq(String.class))).thenReturn(expectedConnectionId);
        String actualConnectionId = sessionExtension.getConnectionId(mockConnection);
        assertEquals(expectedConnectionId, actualConnectionId);

        when(mockConnection.queryForObject(anyString(), eq(String.class))).thenThrow(new SQLException());
        when(mockConnection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) mockConnection).getServerThreadId()).thenReturn(123L);
        actualConnectionId = sessionExtension.getConnectionId(mockConnection);
        assertEquals("123", actualConnectionId);
    }
}