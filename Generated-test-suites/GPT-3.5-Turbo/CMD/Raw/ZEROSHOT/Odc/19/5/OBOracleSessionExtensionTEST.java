import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setup() {
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        obOracleSessionExtension.killQuery(connection, connectionId);

        // verify if execute method is called with correct query
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "TEST_SCHEMA";

        // define current schema
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("OTHER_SCHEMA");

        obOracleSessionExtension.switchSchema(connection, schemaName);

        // verify if update method is called with correct alter SQL
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).update("ALTER SESSION SET CURRENT_SCHEMA='TEST_SCHEMA'");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);

        // define behavior of queryForObject method
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class)))
                .thenReturn("TEST_SCHEMA");

        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);

        // assert if current schema is retrieved correctly
        assertEquals("TEST_SCHEMA", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);

        // mock behavior for OceanBaseConnection
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(12345);

        String connectionId = obOracleSessionExtension.getConnectionId(connection);

        // assert if connection ID is retrieved correctly
        assertEquals("12345", connectionId);
    }
}