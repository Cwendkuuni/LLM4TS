import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;
import org.pf4j.TestExtension;
import org.pf4j.TestExtensionManager;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(PowerMockRunner.class)
@PrepareForTest({OBOracleSessionExtension.class})
public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private SessionExtensionPoint sessionExtensionPoint;
    private ExtensionManager extensionManager;
    private TestExtension testExtension;

    @Before
    public void setup() {
        testExtension = new TestExtension();
        extensionManager = new TestExtensionManager();
        extensionManager.setExtensions(new PluginWrapper[]{testExtension});
        PowerMockito.mockStatic(ExtensionManager.class);
        sessionExtensionPoint = PowerMockito.spy(new OBOracleSessionExtension());
        given(ExtensionManager.getInstance()).willReturn(extensionManager);
    }

    @org.junit.Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "test_connection_id";
        PowerMockito.doNothing().when(sessionExtensionPoint).killQuery(connection, connectionId);

        sessionExtensionPoint.killQuery(connection, connectionId);

        verify(sessionExtensionPoint).killQuery(connection, connectionId);
    }

    @org.junit.Test
    public void testKillQuery_Exception() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "test_connection_id";
        PowerMockito.doThrow(new SQLException("Test exception")).when(sessionExtensionPoint).killQuery(connection, connectionId);

        Assertions.assertThrows(SQLException.class, () -> sessionExtensionPoint.killQuery(connection, connectionId));

        verify(sessionExtensionPoint).killQuery(connection, connectionId);
    }

    @org.junit.Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema_name";

        String currentSchema = "test_current_schema";
        given(sessionExtensionPoint.getCurrentSchema(connection)).willReturn(currentSchema);

        sessionExtensionPoint.switchSchema(connection, schemaName);

        verify(sessionExtensionPoint).killQuery(connection, eq("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName)));
        verify(sessionExtensionPoint, Mockito.never()).getCurrentSchema(connection);
    }

    @org.junit.Test
    public void testSwitchSchema_SameSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema_name";

        String currentSchema = schemaName;
        given(sessionExtensionPoint.getCurrentSchema(connection)).willReturn(currentSchema);

        sessionExtensionPoint.switchSchema(connection, schemaName);

        verify(sessionExtensionPoint, Mockito.never()).killQuery(connection, anyString());
    }

    @org.junit.Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema_name";

        String currentSchema = "test_current_schema";
        String result = "test_result";
        given(sessionExtensionPoint.getCurrentSchema(connection)).willReturn(currentSchema);
        PowerMockito.doReturn(result).when(JdbcOperationsUtil.getJdbcOperations(connection)).queryForObject(anyString(), eq(String.class));

        Assertions.assertEquals(currentSchema, sessionExtensionPoint.getCurrentSchema(connection));
        Assertions.assertEquals(result, sessionExtensionPoint.getCurrentSchema(connection));
    }

    @org.junit.Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "test_connection_id";

        String result = "test_result";
        PowerMockito.doReturn(result).when(JdbcOperationsUtil.getJdbcOperations(connection)).queryForObject(anyString(), eq(String.class));

        Assertions.assertEquals(connectionId, sessionExtensionPoint.getConnectionId(connection));
        Assertions.assertEquals(result, sessionExtensionPoint.getConnectionId(connection));

        if (connection instanceof OceanBaseConnection) {
            String serverThreadId = "test_server_thread_id";
            PowerMockito.doReturn(serverThreadId).when(connection).getServerThreadId();
            Assertions.assertEquals(serverThreadId, sessionExtensionPoint.getConnectionId(connection));
        }
    }

    @org.junit.Test
    public void testGetConnectionId_Null() throws SQLException {
        Connection connection = mock(Connection.class);

        Optional<String> resultOptional = Optional.ofNullable(sessionExtensionPoint.getConnectionId(connection));
        Assertions.assertFalse(resultOptional.isPresent());
    }
}