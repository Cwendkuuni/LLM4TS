import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.sql.Connection;

public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connection;

    @Test
    public void testKillQuery() {
        OBMySQLSessionExtension sessionExt = new OBMySQLSessionExtension();
        doNothing().when(JdbcOperationsUtil.getJdbcOperations(connection)).execute(anyString());
        sessionExt.killQuery(connection, "1");
        verify(JdbcOperationsUtil.getJdbcOperations(connection), times(1)).execute(anyString());
    }
    
    @Test
    public void testSwitchSchema() throws SQLException {
        OBMySQLSessionExtension sessionExt = new OBMySQLSessionExtension();
        when(sessionExt.getCurrentSchema(connection)).thenReturn("oldSchema");
        doNothing().when(connection).setCatalog(anyString());
        sessionExt.switchSchema(connection, "newSchema");
        verify(connection, times(1)).setCatalog(anyString());
    }
    
    @Test
    public void testGetConnectionId() {
        OBMySQLSessionExtension sessionExt = new OBMySQLSessionExtension();
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class)))
                    .thenReturn("1");
        assertEquals("1", sessionExt.getConnectionId(connection));
    }
    
    @Test
    public void testGetCurrentSchema() {
        OBMySQLSessionExtension sessionExt = new OBMySQLSessionExtension();
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class)))
                    .thenReturn("schema");
        assertEquals("schema", sessionExt.getCurrentSchema(connection));
    }
}