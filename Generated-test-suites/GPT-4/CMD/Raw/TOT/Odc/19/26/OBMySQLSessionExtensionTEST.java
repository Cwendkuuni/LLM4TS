import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;

import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {
    OBMySQLSessionExtension obMySQLSessionExtension;

    @Mock
    private Connection connection;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "1";
        obMySQLSessionExtension.killQuery(connection, connectionId);
        verify(connection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws Exception {
        String schemaName = "testSchema";
        when(connection.getCatalog()).thenReturn(schemaName);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        verify(connection, times(1)).getCatalog();
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT DATABASE()";
        obMySQLSessionExtension.getCurrentSchema(connection);
        verify(connection, times(1)).queryForObject(querySql, String.class);
    }

    @Test
    public void testGetConnectionId() {
        String querySql = "select connection_id()";
        obMySQLSessionExtension.getConnectionId(connection);
        verify(connection, times(1)).queryForObject(querySql, String.class);
    }
}