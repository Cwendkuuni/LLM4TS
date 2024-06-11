import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension sessionExtension;
    private Connection connection;

    @Before
    public void setUp() {
        sessionExtension = new OBOracleSessionExtension();
        connection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        try {
            sessionExtension.killQuery(connection, "1234");
        } catch (SQLException e) {
            fail("Should not throw exception");
        }
        verify(JdbcOperationsUtil.getJdbcOperations(connection), times(1)).execute("KILL QUERY 1234");
    }

    @Test
    public void testSwitchSchema() {
        try {
            sessionExtension.switchSchema(connection, "test_schema");
        } catch (SQLException e) {
            fail("Should not throw exception");
        }
        verify(JdbcOperationsUtil.getJdbcOperations(connection), times(1)).update("ALTER SESSION SET CURRENT_SCHEMA='test_schema'");
    }

    @Test
    public void testGetCurrentSchema() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn("test_schema");
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        assertEquals("test_schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException());
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        
        String connectionId = sessionExtension.getConnectionId(connection);
        assertNotNull(connectionId);
        assertTrue(connectionId.matches("\\d+"));
    }
}