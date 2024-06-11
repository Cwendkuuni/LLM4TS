import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import static org.mockito.Mockito.when;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection connection;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        connection = Mockito.mock(OceanBaseConnection.class);
    }

    @Test
    public void testKillQuery() {
        // No exception means pass
        obOracleSessionExtension.killQuery(connection, "001");
    }

    @Test
    public void testSwitchSchema() throws Exception {
        String schemaName = "newSchema";
        when(connection.getSchema()).thenReturn("oldSchema");
        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaSameSchema() throws Exception {
        String schemaName = "oldSchema";
        when(connection.getSchema()).thenReturn(schemaName);
        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "currentSchema";
        when(connection.getSchema()).thenReturn(expectedSchema);
        String actualSchema = obOracleSessionExtension.getCurrentSchema(connection);
        Assert.assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        String expectedConnectionId = "001";
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(Long.parseLong(expectedConnectionId));
        String actualConnectionId = obOracleSessionExtension.getConnectionId(connection);
        Assert.assertEquals(expectedConnectionId, actualConnectionId);
    }
    
    // Add more edge cases and error scenario tests
}