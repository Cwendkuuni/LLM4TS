import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.killQuery(connection, "12345");
        
        verify(connection, times(1)).setCatalog("KILL QUERY 12345");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.switchSchema(connection, "test_schema");

        verify(connection, times(1)).setCatalog("test_schema");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("test_schema");
        
        assertEquals("test_schema", sessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(12345L);
        
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String connectionId = sessionExtension.getConnectionId(connection);
        
        assertEquals("12345", connectionId);
    }

}