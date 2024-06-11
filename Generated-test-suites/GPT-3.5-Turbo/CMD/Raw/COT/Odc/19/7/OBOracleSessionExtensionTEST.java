import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        obOracleSessionExtension.killQuery(connection, "123");
        // No assertion needed for void method
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        obOracleSessionExtension.switchSchema(connection, "schemaName");
        // No assertion needed for void method
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
                .thenReturn("testSchema");
        
        assertEquals("testSchema", obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class))
                .thenReturn("12345");
        
        assertEquals("12345", obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithException() {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class))
                .thenThrow(new RuntimeException());

        // Mock OceanBaseConnection
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(9999L);
        assertEquals("9999", obOracleSessionExtension.getConnectionId(oceanBaseConnection));
    }
}