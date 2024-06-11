import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        // Testing killQuery method
        assertEquals("KILL QUERY 123", obMySQLSessionExtension.killQuery(connection, "123"));
    }

    @Test
    public void testSwitchSchema() {
        // Testing switchSchema method
        connection.setCatalog("InitialSchema");
        obMySQLSessionExtension.switchSchema(connection, "NewSchema");
        assertEquals("NewSchema", connection.getCatalog());
    }

    @Test
    public void testGetCurrentSchema() {
        // Testing getCurrentSchema method
        when(jdbcOperations.queryForObject(eq("SELECT DATABASE()"), eq(String.class))).thenReturn("TestSchema");
        assertEquals("TestSchema", obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        // Testing getConnectionId method
        when(jdbcOperations.queryForObject(eq("select connection_id()"), eq(String.class))).thenReturn("123");
        assertEquals("123", obMySQLSessionExtension.getConnectionId(connection));

        // Testing getConnectionId with OceanBaseConnection and exception
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(456L);
        when(jdbcOperations.queryForObject(eq("select connection_id()"), eq(String.class))).thenThrow(new RuntimeException());
        assertEquals("456", obMySQLSessionExtension.getConnectionId(oceanBaseConnection));
    }
}