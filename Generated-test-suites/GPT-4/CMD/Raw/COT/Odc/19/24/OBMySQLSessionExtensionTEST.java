
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import java.sql.Connection;
import java.sql.SQLException;
import com.oceanbase.jdbc.OceanBaseConnection;
import org.junit.Before;
import org.junit.Test;

public class OBMySQLSessionExtensionTest {
    
    private OBMySQLSessionExtension sessionExtension;
    private Connection mockConnection;
    private OceanBaseConnection mockOceanBaseConnection;

    @Before
    public void setUp() {
        sessionExtension = new OBMySQLSessionExtension();
        mockConnection = mock(Connection.class);
        mockOceanBaseConnection = mock(OceanBaseConnection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "123";
        sessionExtension.killQuery(mockConnection, connectionId);
        verify(mockConnection).execute("KILL QUERY " + connectionId);
    }
    
    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "Test_Schema";
        when(mockConnection.getCatalog()).thenReturn(schemaName);
        sessionExtension.switchSchema(mockConnection, schemaName);
        verify(mockConnection).setCatalog(schemaName);
    }

    @Test
    public void testGetConnectionId() {
        String expectedId = "123";
        when(mockConnection.queryForObject("select connection_id()", String.class)).thenReturn(expectedId);
        String actualId = sessionExtension.getConnectionId(mockConnection);
        assertEquals(expectedId, actualId);
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() {
        when(mockOceanBaseConnection.getServerThreadId()).thenReturn(456L);
        String actualId = sessionExtension.getConnectionId(mockOceanBaseConnection);
        assertEquals("456", actualId);
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "Test_Schema";
        when(mockConnection.queryForObject("SELECT DATABASE()", String.class)).thenReturn(expectedSchema);
        String actualSchema = sessionExtension.getCurrentSchema(mockConnection);
        assertEquals(expectedSchema, actualSchema);
    }
}
