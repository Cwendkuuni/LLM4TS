import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() {
        connection = new OceanBaseConnection(); //mock connection
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        // Typical use case
        obMySQLSessionExtension.killQuery(connection, "1");

        // Edge case
        obMySQLSessionExtension.killQuery(connection, "999999999999");

        // Error scenario
        assertThrows(Exception.class, () -> obMySQLSessionExtension.killQuery(null, "1"));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        // Typical use case
        obMySQLSessionExtension.switchSchema(connection, "schema");

        // Edge case
        obMySQLSessionExtension.switchSchema(connection, null);

        // Error scenario
        assertThrows(SQLException.class, () -> obMySQLSessionExtension.switchSchema(null, "schema"));
    }

    @Test
    public void testGetCurrentSchema() {
        // Typical use case
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertNotNull(currentSchema);

        // Edge case
        connection = null;
        String currentSchemaNull = obMySQLSessionExtension.getCurrentSchema(connection);
        assertNull(currentSchemaNull);
    }

    @Test
    public void testGetConnectionId() {
        // Typical use case
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertNotNull(connectionId);

        // Edge case
        Connection mockConnection = new OceanBaseConnection();
        String connectionIdMock = obMySQLSessionExtension.getConnectionId(mockConnection);
        assertNotNull(connectionIdMock);

        // Error scenario
        assertNull(obMySQLSessionExtension.getConnectionId(null));
    }
}