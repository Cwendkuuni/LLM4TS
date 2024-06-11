import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@Extension
public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setup() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        obMySQLSessionExtension.killQuery(connection, "123");
        // Test the functionality of killQuery by verifying the execution of the query
        verify(JdbcOperationsUtil.getJdbcOperations(connection), times(1)).execute("KILL QUERY 123");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        obMySQLSessionExtension.switchSchema(connection, "test_schema");
        // Test the functionality of switchSchema by verifying the setCatalog method call
        verify(connection, times(1)).setCatalog("test_schema");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("test_schema");
        assertEquals("test_schema", obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(OceanBaseConnection.class);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(456L);
        assertEquals("456", obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaSameSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("test_schema");
        obMySQLSessionExtension.switchSchema(connection, "test_schema");
    }

    @Test
    public void testGetConnectionIdException() {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException());
        assertNull(obMySQLSessionExtension.getConnectionId(connection));
    }

}