import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.*;

public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setup() {
        connection = mock(Connection.class);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() throws Exception {
        obMySQLSessionExtension.killQuery(connection, "testConnectionId");
        verify(connection).createStatement();
        verify(connection).execute("KILL QUERY testConnectionId");
    }

    @Test
    public void testKillQueryWithNullConnection() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.killQuery(null, "testConnectionId"));
    }

    @Test
    public void testKillQueryWithEmptyConnectionId() {
        assertThrows(IllegalArgumentException.class, () -> obMySQLSessionExtension.killQuery(connection, ""));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        obMySQLSessionExtension.switchSchema(connection, "testSchemaName");
        verify(connection).setCatalog("testSchemaName");
    }

    @Test
    public void testSwitchSchemaWithNullConnection() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.switchSchema(null, "testSchemaName"));
    }

    @Test
    public void testSwitchSchemaWithEmptySchemaName() {
        assertThrows(IllegalArgumentException.class, () -> obMySQLSessionExtension.switchSchema(connection, ""));
    }

    @Test
    public void testSwitchSchemaWithExistingSchemaName() throws SQLException {
        Mockito.when(connection.getCatalog()).thenReturn("testSchemaName");
        obMySQLSessionExtension.switchSchema(connection, "testSchemaName");
        verify(connection, Mockito.never()).setCatalog("testSchemaName");
    }

    @Test
    public void testGetCurrentSchema() {
        Mockito.when(connection.queryForObject("SELECT DATABASE()", String.class)).thenReturn("testSchemaName");
        String result = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals("testSchemaName", result);
    }

    @Test
    public void testGetCurrentSchemaWithNullConnection() {
        assertNull(obMySQLSessionExtension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionId() {
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals("testConnectionId", connectionId);
    }

    @Test
    public void testGetConnectionIdWithNullConnection() {
        assertNull(obMySQLSessionExtension.getConnectionId(null));
    }
}

//