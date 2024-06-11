
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

// Indicates that this line give an extension point to some classes that want to enhance or extend your test class.
@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connection;

    @Mock
    private OceanBaseConnection oceanBaseConnection;

    private final String schemaName = "test";
    private final String connectionId = "1";
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setup() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        obMySQLSessionExtension.killQuery(connection, connectionId);
        verify(connection, times(1)).createStatement();
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(connection.getCatalog()).thenReturn(schemaName);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        verify(connection, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        obMySQLSessionExtension.getCurrentSchema(connection);
        verify(connection, times(1)).createStatement();
    }

    @Test
    public void testGetConnectionId() {
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(1L);
        obMySQLSessionExtension.getConnectionId(connection);
        verify(connection, times(1)).createStatement();
    }

    @Test
    public void testGetConnectionIdWithException() {
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(oceanBaseConnection.getServerThreadId()).thenThrow(new RuntimeException());
        obMySQLSessionExtension.getConnectionId(connection);
        verify(connection, times(1)).createStatement();
    }
}
