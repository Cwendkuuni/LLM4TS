import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.SQLException;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = Mockito.mock(Connection.class);
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        
        sessionExtension.killQuery(connection, "123");
        
        // Verify that JdbcOperationsUtil.getJdbcOperations(connection).execute() is called with "KILL QUERY 123"
        Mockito.verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY 123");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        
        Mockito.when(sessionExtension.getCurrentSchema(connection)).thenReturn("schema1");
        
        // Switch to a different schema
        sessionExtension.switchSchema(connection, "schema2");
        
        // Verify that JdbcOperationsUtil.getJdbcOperations(connection).update() is called with "ALTER SESSION SET CURRENT_SCHEMA='schema2'"
        Mockito.verify(JdbcOperationsUtil.getJdbcOperations(connection)).update("ALTER SESSION SET CURRENT_SCHEMA='schema2'");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = Mockito.mock(Connection.class);
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn("schema1");
        
        String result = sessionExtension.getCurrentSchema(connection);
        
        assertEquals("schema1", result);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = Mockito.mock(Connection.class);
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenThrow(new SQLException());
        
        Mockito.when(connection instanceof OceanBaseConnection).thenReturn(true);
        Mockito.when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(123L);
        
        String result = sessionExtension.getConnectionId(connection);
        
        assertEquals("123", result);
    }
}