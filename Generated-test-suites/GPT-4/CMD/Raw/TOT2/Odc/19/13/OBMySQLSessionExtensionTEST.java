import static org.junit.Assert.*;
import org.junit.Test;
import java.sql.Connection;
import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
    private Connection mockConnection = mock(Connection.class);

    @Test
    public void testKillQuery() {
        // You will need to add an actual test implementation
    }

    @Test(expected = NullPointerException.class)
    public void testKillQueryWithNullException() {
        obMySQLSessionExtension.killQuery(null, "12345");
    }

    @Test
    public void testSwitchSchema() {
        // You will need to add an actual test implementation
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() {
        doThrow(new SQLException()).when(mockConnection).setCatalog(anyString());
        obMySQLSessionExtension.switchSchema(mockConnection, "test");
    }

    @Test
    public void testGetCurrentSchema() {
       // You will need to add an actual test implementation
    }

    @Test(expected = NullPointerException.class)
    public void testGetCurrentSchemaWithNullException() {
        obMySQLSessionExtension.getCurrentSchema(null);
    }

    @Test
    public void testGetConnectionId() {
        // You will need to add an actual test implementation
    }

    @Test(expected = NullPointerException.class)
    public void testGetConnectionIdWithNullException() {
        obMySQLSessionExtension.getConnectionId(null);
    }
}