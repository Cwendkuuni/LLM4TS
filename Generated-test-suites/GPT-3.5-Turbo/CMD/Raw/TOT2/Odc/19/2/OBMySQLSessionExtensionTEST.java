import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.SQLException;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    OBMySQLSessionExtension obMySQLSessionExtension;

    @Mock
    Connection connection;

    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        obMySQLSessionExtension.killQuery(connection, "1");
        // Verify if the query "KILL QUERY 1" is executed on the connection object
        Mockito.verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY 1");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        // Test switching to a different schema
        obMySQLSessionExtension.switchSchema(connection, "testSchema");
        Mockito.verify(connection).setCatalog("testSchema");

        // Test switching to the same schema
        obMySQLSessionExtension.switchSchema(connection, "testSchema");
        Mockito.verify(connection, Mockito.never()).setCatalog(Mockito.anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("testSchema");

        assertEquals("testSchema", obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn("123");

        assertEquals("123", obMySQLSessionExtension.getConnectionId(connection));

        // Testing exception handling for OceanBaseConnection
        Connection oceanBaseConnection = Mockito.mock(OceanBaseConnection.class);
        Mockito.when(((OceanBaseConnection) oceanBaseConnection).getServerThreadId()).thenReturn(456L);

        assertEquals("456", obMySQLSessionExtension.getConnectionId(oceanBaseConnection));
    }
}