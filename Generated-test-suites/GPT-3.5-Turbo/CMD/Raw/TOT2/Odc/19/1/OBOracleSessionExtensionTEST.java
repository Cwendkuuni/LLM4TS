import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;

    @Mock
    private Connection mockConnection;

    @Before
    public void setup() {
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() throws SQLException {
        OBOracleSessionExtension obOracleSessionExtensionSpy = spy(obOracleSessionExtension);
        doNothing().when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).execute(anyString());

        obOracleSessionExtensionSpy.killQuery(mockConnection, "123");
        // Add assertions here
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        OBOracleSessionExtension obOracleSessionExtensionSpy = spy(obOracleSessionExtension);
        String schemaName = "test_schema";
        when(obOracleSessionExtensionSpy.getCurrentSchema(mockConnection)).thenReturn("old_schema");

        obOracleSessionExtensionSpy.switchSchema(mockConnection, schemaName);
        // Add assertions here
    }

    @Test
    public void testGetCurrentSchema() {
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(anyString(), eq(String.class))).thenReturn("test_schema");

        String currentSchema = obOracleSessionExtension.getCurrentSchema(mockConnection);
        // Add assertions here
    }

    @Test
    public void testGetConnectionId() {
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(anyString(), eq(String.class))).thenThrow(new SQLException());
        when(mockConnection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) mockConnection).getServerThreadId()).thenReturn(123);

        String connectionId = obOracleSessionExtension.getConnectionId(mockConnection);
        // Add assertions here
    }
}