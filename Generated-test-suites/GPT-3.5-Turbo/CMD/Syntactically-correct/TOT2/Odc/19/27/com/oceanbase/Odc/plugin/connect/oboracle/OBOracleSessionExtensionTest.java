import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

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

        // Verify that the correct SQL query was executed
        verify(connection, times(1)).prepareStatement("KILL QUERY 12345");
    }

    @Test
    public void testSwitchSchema() {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";

        assertThrows(SQLException.class, () -> obOracleSessionExtension.switchSchema(connection, schemaName));
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);

        // Mock the JDBC operation to return a specific schema name
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
                .thenReturn("schemaName");

        assertEquals("schemaName", obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);

        // Mock the JDBC operation to throw an exception
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class))
                .thenThrow(new RuntimeException());

        // Mock the behavior for OceanBaseConnection
        when(oceanBaseConnection.getServerThreadId()).thenReturn(12345);
        when(connection instanceof OceanBaseConnection).thenReturn(true);

        assertEquals("12345", obOracleSessionExtension.getConnectionId(connection));
    }
}