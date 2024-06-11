import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.pf4j.ExtensionManager;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(PowerMockRunner.class)
public class OBMySQLSessionExtensionTest {

    private ExtensionManager extensionManager;
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() {
        extensionManager = Mockito.mock(ExtensionManager.class);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        PowerMockito.mockStatic(JdbcOperationsUtil.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        String connectionId = "conn_id";
        Mockito.when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(Mockito.mock(JdbcOperationsUtil.class));

        obMySQLSessionExtension.killQuery(connection, connectionId);
        Mockito.verify(connection).createStatement();
        Mockito.verify(connection).createStatement().execute("KILL QUERY " + connectionId);
    }

    @Test(expected = SQLException.class)
    public void testKillQuery_Exception() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        String connectionId = "conn_id";
        Mockito.when(connection.createStatement()).thenThrow(new SQLException("Error message"));

        obMySQLSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        String schemaName = "test_schema";
        String currentSchema = "current_schema";

        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(Mockito.mock(JdbcOperationsUtil.class));
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn(currentSchema);

        obMySQLSessionExtension.switchSchema(connection, schemaName);
        Mockito.verify(connection).setCatalog(schemaName);
    }

    @Test
    public void testSwitchSchema_SameSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        String schemaName = "test_schema";
        String currentSchema = "test_schema";

        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(Mockito.mock(JdbcOperationsUtil.class));
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn(currentSchema);

        obMySQLSessionExtension.switchSchema(connection, schemaName);
        Mockito.verifyNoMoreInteractions(connection);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        String schemaName = "test_schema";

        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(Mockito.mock(JdbcOperationsUtil.class));
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn(schemaName);

        assertEquals(schemaName, obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        String connectionId = "conn_id";

        Mockito.when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(Mockito.mock(JdbcOperationsUtil.class));
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn(null);

        assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(connection));

        Mockito.when(connection instanceof OceanBaseConnection).thenReturn(true);
        obMySQLSessionExtension.getConnectionId(connection);
        Mockito.verify(connection).getServerThreadId();
    }

    @Test
    public void testGetConnectionId_Null() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);

        Mockito.when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(Mockito.mock(JdbcOperationsUtil.class));
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn(null);

        assertNull(obMySQLSessionExtension.getConnectionId(connection));
    }
}