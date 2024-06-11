import static org.junit.Assert.*;
import org.junit.Test;
import org.pf4j.Extension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.SessionExtensionPoint;
import com.oceanbase.jdbc.OceanBaseConnection;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        SessionExtensionPoint obMySQLSessionExtension = new OBMySQLSessionExtension();
        Connection connection = null;
        String connectionId = "test_connection_id";
        obMySQLSessionExtension.killQuery(connection, connectionId);

        // Test with null connection
        obMySQLSessionExtension.killQuery(null, connectionId);

        // Test with empty connectionId
        obMySQLSessionExtension.killQuery(connection, "");

        // Test with non-existent connectionId
        obMySQLSessionExtension.killQuery(connection, "NonExistent_connectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        SessionExtensionPoint obMySQLSessionExtension = new OBMySQLSessionExtension();
        Connection connection = null;
        String schemaName = "test_schema";
        obMySQLSessionExtension.switchSchema(connection, schemaName);

        // Test with null connection
        obMySQLSessionExtension.switchSchema(null, schemaName);

        // Test with empty schemaName
        obMySQLSessionExtension.switchSchema(connection, "");

        // Test with schemaName that already is the current schema
        String expectedCurrentSchema = "test_schema";
        String actualCurrentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        if (Objects.equals(expectedCurrentSchema, actualCurrentSchema)) {
            return;
        }
        obMySQLSessionExtension.switchSchema(connection, expectedCurrentSchema);

        // Test with invalid schemaName that does not exist in the database
        obMySQLSessionExtension.switchSchema(connection, "Invalid_schemaName");
    }

    @Test
    public void testGetCurrentSchema() {
        SessionExtensionPoint obMySQLSessionExtension = new OBMySQLSessionExtension();
        Connection connection = null;
        String expectedSchema = "test_schema";
        String actualSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals(expectedSchema, actualSchema);

        // Test with null connection
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        actualSchema = obMySQLSessionExtension.getCurrentSchema(null);
        assertNull(actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        SessionExtensionPoint obMySQLSessionExtension = new OBMySQLSessionExtension();
        Connection connection = null;
        String expectedConnectionId = "test_connection_id";
        String actualConnectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals(expectedConnectionId, actualConnectionId);

        // Test with null connection
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        actualConnectionId = obMySQLSessionExtension.getConnectionId(null);
        assertNull(actualConnectionId);

        // Test with connection that does not support getting the server thread id
        OceanBaseConnection oceanBaseConnection = new OceanBaseConnection();
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.setConnection(oceanBaseConnection);
        actualConnectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertNull(actualConnectionId);
    }
}