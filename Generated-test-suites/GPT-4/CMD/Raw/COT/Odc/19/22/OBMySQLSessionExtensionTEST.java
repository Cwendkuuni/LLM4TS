
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {
    private OBMySQLSessionExtension obMySqlSessionExtension;
    private Connection connection;
    private JdbcOperationsUtil jdbcOperationsUtil;

    @Before
    public void setUp(){
        obMySqlSessionExtension = new OBMySQLSessionExtension();
        connection = Mockito.mock(Connection.class);
        jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
    }

    // Test killQuery
    @Test
    public void testKillQuery(){
        String connectionId = "1";
        obMySqlSessionExtension.killQuery(connection, connectionId);
        verify(jdbcOperationsUtil, times(1)).execute("KILL QUERY " + connectionId);
    }

    // Test switchSchema
    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "schema";
        when(obMySqlSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
        obMySqlSessionExtension.switchSchema(connection, schemaName);
        // no operation should be done if current schema equals to the schemaname
        verify(connection, times(0)).setCatalog(schemaName);
    }

    // Test getCurrentSchema
    @Test
    public void testGetCurrentSchema() {
        String currentSchema = "current_schema";
        when(jdbcOperationsUtil.queryForObject("SELECT DATABASE()", String.class)).thenReturn(currentSchema);
        assertEquals(currentSchema, obMySqlSessionExtension.getCurrentSchema(connection));
    }

    // Test getConnectionId
    @Test
    public void testGetConnectionId() {
        String connectionId = "1";
        when(jdbcOperationsUtil.queryForObject("select connection_id()", String.class)).thenReturn(connectionId);
        assertEquals(connectionId, obMySqlSessionExtension.getConnectionId(connection));
    }

    // Test getConnectionId with exception
    @Test
    public void testGetConnectionIdWithException() {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(1L);

        String exceptionString = "Exception string";
        when(jdbcOperationsUtil.queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException(exceptionString));

        String actualResult = obMySqlSessionExtension.getConnectionId(oceanBaseConnection);
        assertEquals("1", actualResult);
    }
}
