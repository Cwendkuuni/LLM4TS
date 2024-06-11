
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.SQLException;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @InjectMocks
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Mock
    private Connection mockConnection;

    @Before
    public void setup() {
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(Mockito.mock(JdbcOperationsUtil.class));
    }

    @Test
    public void testKillQuery() {
        obMySQLSessionExtension.killQuery(mockConnection, "123");
        // Add assertions or verify method calls if needed
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Mockito.when(obMySQLSessionExtension.getCurrentSchema(mockConnection)).thenReturn("schema1");
        obMySQLSessionExtension.switchSchema(mockConnection, "schema2");
        // Add assertions or verify method calls if needed
    }

    @Test
    public void testGetCurrentSchema() {
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn("schema");
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(mockConnection);
        assertEquals("schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenThrow(new RuntimeException());
        Mockito.when(mockConnection instanceof OceanBaseConnection).thenReturn(true);
        Mockito.when(((OceanBaseConnection) mockConnection).getServerThreadId()).thenReturn(1L);
        String connectionId = obMySQLSessionExtension.getConnectionId(mockConnection);
        assertNull(connectionId);
    }
}
