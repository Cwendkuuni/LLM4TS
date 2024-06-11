import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;

import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtensionTest;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtensionTest.MockConnection;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtensionTest.MockOceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

public class OBMySQLSessionExtensionTest {

    private ExtensionManager manager;
    private SessionExtensionPoint extension;
    private Connection connection;
    private MockConnection mockConn;
    private MockOceanBaseConnection mockOceanBaseConnection;

    @Before
    public void setUp() {
        PluginWrapper wrapper = new PluginWrapper();
        manager = new ExtensionManager();
        manager.loadExtension(wrapper);
        extension = manager.getExtension(OBMySQLSessionExtension.class);
        connection = Mockito.mock(Connection.class);
        mockConn = new MockConnection();
        mockOceanBaseConnection = new MockOceanBaseConnection();
    }

    @Test
    public void testKillQuery() {
        // Basic test
        testBasicKillQuery(connection);

        // Edge cases and exception handling
        testKillQueryWithNullConnection();
        testKillQueryWithNullConnectionId();
        testKillQueryWithEmptyConnectionId();
        testKillQueryWithNullStatement();
        testKillQueryWithSQLExceptionThrown();
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        // Basic test
        testBasicSwitchSchema(connection);

        // Edge cases and exception handling
        testSwitchSchemaWithNullConnection();
        testSwitchSchemaWithNullSchemaName();
        testSwitchSchemaWithEmptySchemaName();
        testSwitchSchemaWithSQLExceptionThrown();
        testSwitchSchemaWithALTERSCHEMAStatementFailure();
    }

    @Test
    public void testGetCurrentSchema() {
        // Basic test
        testBasicGetCurrentSchema(connection);

        // Edge cases and exception handling
        testGetCurrentSchemaWithNullConnection();
        testGetCurrentSchemaWithSQLExceptionThrown();
    }

    @Test
    public void testGetConnectionId() {
        // Basic test
        testBasicGetConnectionId(connection);

        // Edge cases and exception handling
        testGetConnectionIdWithNullConnection();
        testGetConnectionIdWithSQLExceptionThrown();
        testGetConnectionIdWithNegativeOceanBaseServerThreadId();
    }

    private void testBasicKillQuery(Connection connection) {
        OBMySQLSessionExtension.killQuery(connection, "testConnectionId");
        verify(connection).createStatement();
        verify(connection).executeUpdate("KILL QUERY testConnectionId");
    }

    private void testKillQueryWithNullConnection() {
        OBMySQLSessionExtension.killQuery(null, "testConnectionId");
        verifyNoMoreInteractions(connection);
    }

    private void testKillQueryWithNullConnectionId() {
        OBMySQLSessionExtension.killQuery(connection, null);
        verifyNoMoreInteractions(connection);
    }

    private void testKillQueryWithEmptyConnectionId() {
        OBMySQLSessionExtension.killQuery(connection, "");
        verifyNoMoreInteractions(connection);
    }

    private void testKillQueryWithNullStatement() {
        Statement statement = null;
        OBMySQLSessionExtension.killQuery(connection, "testConnectionId");
        verify(connection).createStatement();
        verify(connection, never()).executeUpdate("KILL QUERY testConnectionId");
        verify(statement, never()).executeUpdate("KILL QUERY testConnectionId");
    }

    private void testKillQueryWithSQLExceptionThrown() {
        when(connection.createStatement()).thenThrow(new SQLException());
        OBMySQLSessionExtension.killQuery(connection, "testConnectionId");
        verify(connection, times(2)).createStatement();
        verify(connection, never()).executeUpdate("KILL QUERY testConnectionId");
    }

    private void testBasicSwitchSchema(Connection connection) {
        OBMySQLSessionExtension.switchSchema(connection, "testSchemaName");
        verify(connection).setCatalog("testSchemaName");
    }

    private void testSwitchSchemaWithNullConnection() {
        OBMySQLSessionExtension.switchSchema(null, "testSchemaName");
        verifyNoMoreInteractions(connection);
    }

    private void testSwitchSchemaWithNullSchemaName() {
        OBMySQLSessionExtension.switchSchema(connection, null);
        verifyNoMoreInteractions(connection);
    }

    private void testSwitchSchemaWithEmptySchemaName() {
        OBMySQLSessionExtension.switchSchema(connection, "");
        verifyNoMoreInteractions(connection);
    }

    private void testSwitchSchemaWithSQLExceptionThrown() throws SQLException {
        when(connection.setCatalog("testSchemaName")).thenThrow(new SQLException());
        OBMySQLSessionExtension.switchSchema(connection, "testSchemaName");
        verify(connection, times(2)).setCatalog("testSchemaName");
        verify(connection, never()).executeUpdate("ALTER SCHEMA testSchemaName RENAME TO testSchemaName_old");
        verify(connection, never()).executeUpdate("CREATE SCHEMA IF NOT EXISTS testSchemaName");
        verify(connection, never()).executeUpdate("USE testSchemaName");
    }

    private void testSwitchSchemaWithALTERSCHEMAStatementFailure() throws SQLException {
        when(connection.setCatalog("testSchemaName")).thenReturn(true);
        doThrow(new SQLException("ALTER SCHEMA statement failed")).when(connection).executeUpdate("ALTER SCHEMA testSchemaName RENAME TO testSchemaName_old");
        doThrow(new SQLException("CREATE SCHEMA statement failed")).when(connection).executeUpdate("CREATE SCHEMA IF NOT EXISTS testSchemaName");
        doThrow(new SQLException("USE SCHEMA statement failed")).when(connection).executeUpdate("USE testSchemaName");
        OBMySQLSessionExtension.switchSchema(connection, "testSchemaName");
        verify(connection, times(4)).setCatalog("testSchemaName");
        verify(connection, times(3)).executeUpdate("ALTER SCHEMA testSchemaName RENAME TO testSchemaName_old");
        verify(connection, times(2)).executeUpdate("CREATE SCHEMA IF NOT EXISTS testSchemaName");
        verify(connection, times(2)).executeUpdate("USE testSchemaName");
    }

    private void testBasicGetCurrentSchema(Connection connection) {
        String currentSchema = OBMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals("currentSchema", currentSchema);
    }

    private void testGetCurrentSchemaWithNullConnection() {
        String currentSchema = OBMySQLSessionExtension.getCurrentSchema(null);
        verifyNoMoreInteractions(connection);
        assertNull(currentSchema);
    }

    private void testGetCurrentSchemaWithSQLExceptionThrown() {
        when(connection.queryForObject("SELECT DATABASE()", String.class)).thenThrow(new SQLException());
        String currentSchema = OBMySQLSessionExtension.getCurrentSchema(connection);
        verify(connection, times(2)).queryForObject("SELECT DATABASE()", String.class);
        assertNull(currentSchema);
    }

    private void testBasicGetConnectionId(Connection connection) {
        OBMySQLSessionExtension.getConnectionId(connection);
        verifyNoMoreInteractions(connection);
    }

    private void testGetConnectionIdWithNullConnection() {
        OBMySQLSessionExtension.getConnectionId(null);
        verifyNoMoreInteractions(connection);
        assertNull(OBMySQLSessionExtension.getConnectionId(connection));
    }

    private void testGetConnectionIdWithSQLExceptionThrown() {
        when(connection.queryForObject("select connection_id()", String.class)).thenThrow(new SQLException());
        String connectionId = OBMySQLSessionExtension.getConnectionId(connection);
        verify(connection, times(2)).queryForObject("select connection_id()", String.class);
        assertNull(connectionId);
    }

    private void testGetConnectionIdWithNegativeOceanBaseServerThreadId() {
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(-1);
        String connectionId = OBMySQLSessionExtension.getConnectionId(connection);
        assertEquals(-1, Integer.parseInt(connectionId));
    }
}

class MockConnection implements Connection {
    @Override
    public Statement createStatement() throws SQLException {
        mockConn.statement = Mockito.mock(Statement.class);
        return mockConn.statement;
    }
}

class MockOceanBaseConnection implements OceanBaseConnection {
    int serverThreadId;

    @Override
    public int getServerThreadId() {
        return serverThreadId;
    }
}