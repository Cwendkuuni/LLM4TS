import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;
import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private OBMySQLSessionExtension obMySQLSessionExtension;
    private String connectionId;
    private String schemaName;

    @Before
    public void setup() {
        connection = mock(Connection.class);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        connectionId = "1234";
        schemaName = "test_schema";
    }

    @Test
    public void testKillQuery() {
        obMySQLSessionExtension.killQuery(connection, connectionId);
        verify(connection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        verify(connection, times(1)).setCatalog(schemaName);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        doThrow(new SQLException()).when(connection).setCatalog(anyString());
        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        obMySQLSessionExtension.getCurrentSchema(connection);
        verify(connection, times(1)).queryForObject("SELECT DATABASE()", String.class);
    }

    @Test
    public void testGetConnectionId() {
        obMySQLSessionExtension.getConnectionId(connection);
        verify(connection, times(1)).queryForObject("select connection_id()", String.class);
    }
}