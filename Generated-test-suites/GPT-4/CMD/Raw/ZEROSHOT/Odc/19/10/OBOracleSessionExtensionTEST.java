
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private Connection connection;
    @Mock
    private JdbcOperationsUtil.JdbcOperations jdbcOperations;
    @Mock
    private OceanBaseConnection oceanBaseConnection;

    private final String SCHEMA_NAME = "schema";
    private final String CONNECTION_ID = "connectionId";

    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        obOracleSessionExtension.killQuery(connection, CONNECTION_ID);
        verify(jdbcOperations).execute("KILL QUERY " + CONNECTION_ID);
    }

    @Test
    public void testSwitchSchema_sameSchema() throws SQLException {
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        when(jdbcOperations.queryForObject(any(), any())).thenReturn(SCHEMA_NAME);
        obOracleSessionExtension.switchSchema(connection, SCHEMA_NAME);
        verify(jdbcOperations, never()).update(any());
    }

    @Test
    public void testSwitchSchema_differentSchema() throws SQLException {
        final String NEW_SCHEMA_NAME = "newSchema";
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        when(jdbcOperations.queryForObject(any(), any())).thenReturn(SCHEMA_NAME);
        obOracleSessionExtension.switchSchema(connection, NEW_SCHEMA_NAME);
        verify(jdbcOperations).update("ALTER SESSION SET CURRENT_SCHEMA=" + NEW_SCHEMA_NAME);
    }

    @Test
    public void testGetCurrentSchema() {
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        when(jdbcOperations.queryForObject(any(), any())).thenReturn(SCHEMA_NAME);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assert(currentSchema.equals(SCHEMA_NAME));
    }

    @Test
    public void testGetConnectionId_noException() {
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        when(jdbcOperations.queryForObject(any(), any())).thenReturn(CONNECTION_ID);
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assert(connectionId.equals(CONNECTION_ID));
    }

    @Test
    public void testGetConnectionId_exceptionThrown() {
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenThrow(new RuntimeException());
        String connectionId = obOracleSessionExtension.getConnectionId(oceanBaseConnection);
        assert(connectionId.equals(String.valueOf(oceanBaseConnection.getServerThreadId())));
    }
}
