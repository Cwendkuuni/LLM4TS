import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();

        sessionExtension.killQuery(connection, "123");

        verify(connection, times(1)).createStatement();
        verify(connection, times(1)).prepareStatement("KILL QUERY 123");
        verify(connection, times(1)).execute("KILL QUERY 123");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();

        sessionExtension.switchSchema(connection, "test_schema");

        verify(connection, times(1)).createStatement();
        verify(connection, times(1)).prepareStatement("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL");
        verify(connection, times(1)).prepareStatement("ALTER SESSION SET CURRENT_SCHEMA='test_schema'");
        verify(connection, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA='test_schema'");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn("test_schema");

        String schema = sessionExtension.getCurrentSchema(connection);
        assertEquals("test_schema", schema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn("123");

        String connectionId = sessionExtension.getConnectionId(connection);
        assertEquals("123", connectionId);
    }
}