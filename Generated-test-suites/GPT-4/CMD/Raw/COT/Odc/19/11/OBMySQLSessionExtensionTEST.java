
import static org.mockito.Mockito.*;
import org.junit.Test;
import java.sql.Connection;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.Assert;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        sessionExtension.killQuery(connection, "1");
        verify(connection, times(1)).prepareStatement("KILL QUERY " + "1");
    }
    
    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.getCatalog()).thenReturn("schema1");
        sessionExtension.switchSchema(connection, "schema2");
        verify(connection, times(1)).setCatalog("schema2");
    }

    @Test(expected = NullPointerException.class)
    public void testSwitchSchemaWithNullInputs() throws SQLException {
        sessionExtension.switchSchema(null, null);
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        sessionExtension.getCurrentSchema(connection);
        verify(connection, times(1)).prepareStatement("SELECT DATABASE()");
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        sessionExtension.getConnectionId(connection);
        verify(connection, times(1)).prepareStatement("select connection_id()");
    }

    @Test
    public void testGetConnectionIdOceanBaseConnection() {
        OceanBaseConnection connection = mock(OceanBaseConnection.class);
        sessionExtension.getConnectionId(connection);
        verify(connection, times(1)).getServerThreadId();
    }
}
