import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.killQuery(connection, "1234");
        // add assertion or verify based on the functionality
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, "test_schema");
        // add assertion or verify based on the functionality
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("test_schema");
        assertEquals("test_schema", extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn("9876");
        String connectionId = extension.getConnectionId(connection);
        assertEquals("9876", connectionId);
    }

    // Add additional test cases for edge cases and exception handling scenarios

}