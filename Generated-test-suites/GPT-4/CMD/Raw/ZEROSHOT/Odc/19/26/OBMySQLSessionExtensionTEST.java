
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

/**
* Unit test for OBMySQLSessionExtension.
*/
@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension;
    private Connection connection;
    private JdbcOperationsUtil jdbcOperationsUtil;

    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        connection = mock(Connection.class);
        jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "123";
        obMySQLSessionExtension.killQuery(connection, connectionId);
        verify(jdbcOperationsUtil).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String currentSchema = "testSchema";
        String newSchema = "newSchema";
        when(jdbcOperationsUtil.queryForObject("SELECT DATABASE()", String.class)).thenReturn(currentSchema);
        obMySQLSessionExtension.switchSchema(connection, newSchema);
        verify(connection).setCatalog(newSchema);
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "testSchema";
        when(jdbcOperationsUtil.queryForObject("SELECT DATABASE()", String.class)).thenReturn(expectedSchema);
        String actualSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        String expectedConnectionId = "123";
        when(jdbcOperationsUtil.queryForObject("select connection_id()", String.class)).thenReturn(expectedConnectionId);
        String actualConnectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals(expectedConnectionId, actualConnectionId);
    }
}
