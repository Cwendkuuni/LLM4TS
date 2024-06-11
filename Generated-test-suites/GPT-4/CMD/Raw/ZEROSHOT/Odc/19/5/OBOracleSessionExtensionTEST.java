import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.sql.Connection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.jdbc.OceanBaseConnection;

public class OBOracleSessionExtensionTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private OceanBaseConnection mockOceanBaseConnection;
    @Mock
    private JdbcOperationsUtil mockJdbcOperationsUtil;

    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        obOracleSessionExtension.killQuery(mockConnection, "TestConnectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(mockJdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(any(String.class), any())
        ).thenReturn("DifferentSchemaName");

        obOracleSessionExtension.switchSchema(mockConnection, "TestSchemaName");
    }

    @Test
    public void testGetCurrentSchema() {
        when(mockJdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(any(String.class), any())
         ).thenReturn("TestSchemaName");

        String schemaName = obOracleSessionExtension.getCurrentSchema(mockConnection);

        assertEquals("TestSchemaName", schemaName);
    }

    @Test
    public void testGetConnectionId() {
        when(mockJdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(any(String.class), any())
        ).thenReturn(null);

        String connectionId = ((OceanBaseConnection) mockConnection).getServerThreadId() + "";

        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(mockConnection));
    }
}