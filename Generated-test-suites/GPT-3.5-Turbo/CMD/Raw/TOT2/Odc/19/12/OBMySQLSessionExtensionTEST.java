import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    Connection mockConnection;

    OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setup() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "1234";
        obMySQLSessionExtension.killQuery(mockConnection, connectionId);
        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(obMySQLSessionExtension.getCurrentSchema(mockConnection)).thenReturn("old_schema");
        
        obMySQLSessionExtension.switchSchema(mockConnection, "new_schema");
        verify(mockConnection).setCatalog("new_schema");
        
        // Test when current schema is already the same as the target schema
        obMySQLSessionExtension.switchSchema(mockConnection, "old_schema");
        verify(mockConnection, never()).setCatalog("old_schema");
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "test_schema";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn(expectedSchema);

        String currentSchema = obMySQLSessionExtension.getCurrentSchema(mockConnection);
        assertEquals(expectedSchema, currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        String expectedConnectionId = "9876";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select connection_id()", String.class))
                .thenReturn(expectedConnectionId);

        String connectionId = obMySQLSessionExtension.getConnectionId(mockConnection);
        assertEquals(expectedConnectionId, connectionId);
        
        // Test exception case
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select connection_id()", String.class))
                .thenThrow(new RuntimeException());
        when(mockConnection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) mockConnection).getServerThreadId()).thenReturn(5432L);
        
        connectionId = obMySQLSessionExtension.getConnectionId(mockConnection);
        assertEquals("5432", connectionId);
    }
}