import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        // Test typical use case
        Connection connection = Mockito.mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.killQuery(connection, "123");
        verify(connection, times(1)).prepareStatement("KILL QUERY 123");

        // Test edge case
        assertNull(extension.killQuery(null, "123"));
    }

    @Test
    public void testSwitchSchema() {
        // Test typical use case
        Connection connection = Mockito.mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();

        // Mock getCurrentSchema method
        when(extension.getCurrentSchema(connection)).thenReturn("schema1");

        try {
            extension.switchSchema(connection, "schema2");
            verify(connection, times(1)).prepareStatement("ALTER SESSION SET CURRENT_SCHEMA='schema2'");
        } catch (SQLException e) {
            fail("Switch schema should not throw an exception");
        }

        // Test error scenario
        // Mock getCurrentSchema method
        when(extension.getCurrentSchema(connection)).thenThrow(new SQLException());
        try {
            extension.switchSchema(connection, "schema2");
            fail("Switch schema should throw an exception");
        } catch (SQLException e) {
            // Test passed
        }
    }

    @Test
    public void testGetCurrentSchema() {
        // Test typical use case
        Connection connection = Mockito.mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();

        // Mock JdbcOperationsUtil.getJdbcOperations method
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn("schema1");

        assertEquals("schema1", extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        // Test typical use case
        Connection connection = Mockito.mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();

        // Mock JdbcOperationsUtil.getJdbcOperations method
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn("123");

        assertEquals("123", extension.getConnectionId(connection));

        // Test error scenario
        // Mock OceanBaseConnection class
        OceanBaseConnection oceanBaseConnection = Mockito.mock(OceanBaseConnection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class)).thenThrow(new RuntimeException());
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(456L);

        assertEquals("456", extension.getConnectionId(oceanBaseConnection));
    }

}