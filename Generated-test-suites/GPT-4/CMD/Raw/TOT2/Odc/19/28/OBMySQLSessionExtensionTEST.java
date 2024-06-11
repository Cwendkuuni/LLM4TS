import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import com.oceanbase.jdbc.OceanBaseConnection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {

    OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
    Connection connection = Mockito.mock(Connection.class);

    //Edge cases and Exceptions considerations should be done according to your business rules.

    @Test
    public void testKillQuery() {
        extension.killQuery(connection, "testId");
        // Check if the method was called properly, more assertions can be added as per the business needs.
    }

    @Test
    public void testSwitchSchema() {
        try {
            extension.switchSchema(connection, "testSchema");
            // Check if the method was called properly, more assertions can be added as per the business needs.
        } catch (SQLException e) {
            fail("No exception was expected.");
        }
    }

    @Test
    public void testGetSchema() {
        when(extension.getCurrentSchema(connection)).thenReturn("test");
        assertEquals("test", extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        when(extension.getConnectionId(connection)).thenReturn("testId");
        assertEquals("testId", extension.getConnectionId(connection));
        when(extension.getConnectionId(connection)).thenThrow(new RuntimeException("test exception"));

        Connection obConn = Mockito.mock(OceanBaseConnection.class);
        when(obConn.getServerThreadId()).thenReturn(12345678L);
        assertEquals("12345678", extension.getConnectionId(obConn));
    }
}