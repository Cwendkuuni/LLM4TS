import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OBOracleSessionExtensionTest {

    private Connection connection;
    private OBOracleSessionExtension sessionExtension;

    @BeforeAll
    public void setup() {
        connection =Mockito.mock(Connection.class);
        sessionExtension =new OBOracleSessionExtension();
    }

    @Test
    @DisplayName("testKillQuery")
    public void testKillQuery() {
        String connectionId = "test_connection_id";
        Mockito.when(sessionExtension.killQuery(connection, connectionId)).thenCallRealMethod();
        sessionExtension.killQuery(connection, connectionId);
        Mockito.verify(connection, Mockito.times(1)).createStatement();
        Mockito.verify(connection, Mockito.times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    @DisplayName("testSwitchSchema")
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema_name";
        Mockito.when(sessionExtension.switchSchema(connection, schemaName)).thenCallRealMethod();
        sessionExtension.switchSchema(connection, schemaName);
        Mockito.verify(connection, Mockito.times(1)).createStatement();
        Mockito.verify(connection, Mockito.times(1)).execute("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
    }

    @Test
    @DisplayName("testGetCurrentSchema")
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = "test_schema_name";
        Mockito.when(sessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);
        String returnedSchema = sessionExtension.getCurrentSchema(connection);
        assertEquals(currentSchema, returnedSchema);
    }

    @Test
    @DisplayName("testGetConnectionId")
    public void testGetConnectionId() {
        Mockito.when(connection instanceof OceanBaseConnection).thenReturn(true);
        String connectionId = "test_connection_id";
        String returnedConnectionId = sessionExtension.getConnectionId(connection);
        assertEquals(connectionId, returnedConnectionId);
    }

    @Test
    @DisplayName("testKillQuery_NullConnection")
    public void testKillQuery_NullConnection() {
        String connectionId = "test_connection_id";
        assertThrows(NullPointerException.class, () -> sessionExtension.killQuery(null, connectionId));
    }

    @Test
    @DisplayName("testSwitchSchema_NullConnection")
    public void testSwitchSchema_NullConnection() {
        String schemaName = "test_schema_name";
        assertThrows(NullPointerException.class, () -> sessionExtension.switchSchema(null, schemaName));
    }

    @Test
    @DisplayName("testSwitchSchema_NullSchemaName")
    public void testSwitchSchema_NullSchemaName() {
        Connection connection = Mockito.mock(Connection.class);
        assertThrows(NullPointerException.class, () -> sessionExtension.switchSchema(connection, null));
    }

    @Test
    @DisplayName("testGetConnectionId_NullConnection")
    public void testGetConnectionId_NullConnection() {
        assertThrows(NullPointerException.class, () -> sessionExtension.getConnectionId(null));
    }
}