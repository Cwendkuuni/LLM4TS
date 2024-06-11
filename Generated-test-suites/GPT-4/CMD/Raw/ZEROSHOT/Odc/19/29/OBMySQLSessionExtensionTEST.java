
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;

import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection mockConnection = mock(Connection.class);
        String testConnectionId = "testConnectionId";
        extension.killQuery(mockConnection, testConnectionId);
        
        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection)).execute("KILL QUERY " + testConnectionId);
    }

    @Test
    public void testSwitchSchema() throws Exception {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        
        Connection mockConnection = mock(Connection.class);
        
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("testSchema");
        
        extension.switchSchema(mockConnection, "newSchema");
        verify(mockConnection).setCatalog("newSchema");
    }

    @Test
    public void testGetCurrentSchema() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection mockConnection = mock(Connection.class);

        String expectedSchema = "testSchema";
        
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("SELECT DATABASE()", String.class)).thenReturn(expectedSchema);
        
        String actualSchema = extension.getCurrentSchema(mockConnection);
    
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection mockConnection = mock(Connection.class);

        String expectedConnectionId = "testConnectionId";
        
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select connection_id()", String.class)).thenReturn(expectedConnectionId);
        
        String actualConnectionId = extension.getConnectionId(mockConnection);
        
        assertEquals(expectedConnectionId, actualConnectionId);
    }
}
