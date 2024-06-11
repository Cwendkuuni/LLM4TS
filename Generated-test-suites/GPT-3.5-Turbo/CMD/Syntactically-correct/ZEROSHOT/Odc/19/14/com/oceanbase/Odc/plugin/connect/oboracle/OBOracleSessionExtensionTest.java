import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBOracleSessionExtensionTest {
    
    private OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
    
    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        obOracleSessionExtension.killQuery(connection, "123");
        verify(connection, times(1)).prepareStatement("KILL QUERY 123");
    }
    
    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        obOracleSessionExtension.switchSchema(connection, "schema");
        verify(connection, times(1)).prepareStatement("ALTER SESSION SET CURRENT_SCHEMA='schema'");
    }
    
    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn("schema");
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals("schema", currentSchema);
    }
    
    @Test
    public void testGetConnectionId() {
        Connection connection = mock(OceanBaseConnection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenThrow(new SQLException());
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(123L);
        
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals("123", connectionId);
    }
}