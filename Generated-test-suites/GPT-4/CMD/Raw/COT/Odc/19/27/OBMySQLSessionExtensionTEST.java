import org.junit.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {
    OBMySQLSessionExtension obMySQLSessionExtension;

    @Mock
    Connection connection;

    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        connection = mock(Connection.class);
    }

    @Test(expected = NullPointerException.class)
    public void testKillQueryWithNullConnection() {
        obMySQLSessionExtension.killQuery(null, "123");
    }

    @Test
    public void testKillQuery() {
        obMySQLSessionExtension.killQuery(connection, "123");
        verify(connection, times(1)).execute("KILL QUERY " + "123");
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithNonExistentSchema() throws SQLException {
        when(connection.getCatalog()).thenReturn("existingSchema");
        obMySQLSessionExtension.switchSchema(connection, "nonExistentSchema");
    }

    @Test(expected = NullPointerException.class)
    public void testGetCurrentSchemaWithNullConnection() {
        obMySQLSessionExtension.getCurrentSchema(null);
    }

    @Test
    public void testGetCurrentSchema() {
        when(connection.queryForObject(anyString(), any(Class.class))).thenReturn("schema");
        Assert.assertEquals("schema", obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test(expected = NullPointerException.class)
    public void testGetConnectionIdWithNullConnection() {
        obMySQLSessionExtension.getConnectionId(null);
    }

    @Test
    public void testGetConnectionId() {
        when(connection.queryForObject(anyString(), any(Class.class))).thenReturn("connId");
        Assert.assertEquals("connId", obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithException() {
        when(connection.queryForObject(anyString(), any(Class.class))).thenThrow(new RuntimeException());
        when(connection.getServerThreadId()).thenReturn(567L);
        Assert.assertEquals("567", obMySQLSessionExtension.getConnectionId(connection));
    }
}