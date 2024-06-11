import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connection;

    @Test
    public void testKillQuery() {
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String connectionId = "12345";

        sessionExtension.killQuery(connection, connectionId);

        // Verify the functionality here
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String schemaName = "TestSchema";

        sessionExtension.switchSchema(connection, schemaName);

        // Verify the functionality here
    }

    @Test
    public void testGetCurrentSchema() {
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();

        String currentSchema = sessionExtension.getCurrentSchema(connection);

        // Verify the functionality here
    }

    @Test
    public void testGetConnectionId() {
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String expectedConnectionId = "12345";

        when(connection).thenReturn(mock(OceanBaseConnection.class));
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(12345L);

        String connectionId = sessionExtension.getConnectionId(connection);

        // Verify the functionality here
    }

    // Include additional test cases for edge cases and exception handling scenarios

}