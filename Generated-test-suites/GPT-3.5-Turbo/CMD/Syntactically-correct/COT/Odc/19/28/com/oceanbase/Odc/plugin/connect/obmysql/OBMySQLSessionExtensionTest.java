import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;

public class OBMySQLSessionExtensionTest {

    OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        obMySQLSessionExtension.killQuery(connection, "123");
        // Verify that execute method is called with correct SQL
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY 123");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        obMySQLSessionExtension.switchSchema(connection, "testSchema");
        // Verify that setCatalog method is called with correct schemaName
        verify(connection).setCatalog("testSchema");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("testSchema");
        assertEquals("testSchema", obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn("12345");
        assertEquals("12345", obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdExceptionHandling() throws SQLException {
        Connection connection = mock(Connection.class);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
                .thenThrow(new SQLException("Connection error"));
        
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(54321);

        assertEquals("54321", obMySQLSessionExtension.getConnectionId(connection));
    }
}