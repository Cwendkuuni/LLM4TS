import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.mockito.Mockito;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        // Setup
        Connection connection = Mockito.mock(Connection.class);
        String connectionId = "12345";

        // Test
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.killQuery(connection, connectionId);

        // Verify
        Mockito.verify(connection, Mockito.times(1)).prepareStatement("KILL QUERY 12345");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        // Setup
        Connection connection = Mockito.mock(Connection.class);
        String schemaName = "test_schema";

        // Test
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.switchSchema(connection, schemaName);

        // Verify
        Mockito.verify(connection, Mockito.times(1)).prepareStatement("ALTER SESSION SET CURRENT_SCHEMA='test_schema'");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        // Setup
        Connection connection = Mockito.mock(Connection.class);
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn("schema_test");

        // Test
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        String currentSchema = extension.getCurrentSchema(connection);

        // Verify
        assertNotNull(currentSchema);
        assertEquals("schema_test", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        // Setup
        Connection connection = Mockito.mock(Connection.class);
        String expectedConnectionId = "54321";
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn(expectedConnectionId);

        // Test
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        String connectionId = extension.getConnectionId(connection);

        // Verify
        assertNotNull(connectionId);
        assertEquals(expectedConnectionId, connectionId);
    }

    @Test
    public void testGetConnectionIdWhenExceptionThrown() throws SQLException {
        // Setup
        Connection connection = Mockito.mock(Connection.class);
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class)).thenThrow(new SQLException());

        // Test
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        String connectionId = extension.getConnectionId(connection);

        // Verify
        assertNull(connectionId);
    }
}