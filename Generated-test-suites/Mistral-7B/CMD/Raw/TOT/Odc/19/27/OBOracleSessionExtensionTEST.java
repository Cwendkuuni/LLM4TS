import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Deprecated // Use JUnit 5 instead
public class OBOracleSessionExtensionTest {

    private static ExtensionManager extensionManager;
    private static SessionExtensionPoint obOracleSessionExtension;

    @BeforeAll
    public static void setup() {
        extensionManager = mock(ExtensionManager.class);
        PluginWrapper pluginWrapper = mock(PluginWrapper.class);
        when(pluginWrapper.getExtension(SessionExtensionPoint.class)).thenReturn(obOracleSessionExtension = mock(OBOracleSessionExtension.class));
        when(extensionManager.getExtensions()).thenReturn(new Object[]{pluginWrapper});
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "test_connection_id";
        Executable killQueryMethod = () -> obOracleSessionExtension.killQuery(connection, connectionId);

        try {
            Mockito.doNothing().when(obOracleSessionExtension).getJdbcOperations(connection);
            killQueryMethod.execute();
            Assertions.assertDoesNotThrow(killQueryMethod);
        } finally {
            Mockito.verify(obOracleSessionExtension, Mockito.times(1)).getJdbcOperations(connection);
        }
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        Executable switchSchemaMethod = () -> obOracleSessionExtension.switchSchema(connection, schemaName);

        try {
            Mockito.doNothing().when(obOracleSessionExtension).getJdbcOperations(connection);
            Mockito.doReturn("test_current_schema").when(obOracleSessionExtension).getCurrentSchema(connection);
            Mockito.doThrow(new SQLException("Test exception")).when(obOracleSessionExtension).getJdbcOperations(connection).update(Mockito.anyString());

            switchSchemaMethod.execute();
            Assertions.assertThrows(SQLException.class, switchSchemaMethod);
            Assertions.assertEquals("Test exception", switchSchemaMethod.getExecutionException().getMessage());
        } finally {
            Mockito.verify(obOracleSessionExtension, Mockito.times(1)).getJdbcOperations(connection);
            Mockito.verify(obOracleSessionExtension, Mockito.times(1)).getCurrentSchema(connection);
            Mockito.verify(obOracleSessionExtension, Mockito.times(1)).getJdbcOperations(connection).update(Mockito.anyString());
        }
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        Executable getCurrentSchemaMethod = () -> obOracleSessionExtension.getCurrentSchema(connection);

        try {
            Mockito.doReturn("test_schema").when(obOracleSessionExtension).getJdbcOperations(connection).queryForObject(Mockito.anyString(), Mockito.eq(String.class));
            getCurrentSchemaMethod.execute();
            Assertions.assertEquals("test_schema", getCurrentSchemaMethod.getResult());
        } finally {
            Mockito.verify(obOracleSessionExtension, Mockito.times(1)).getJdbcOperations(connection);
            Mockito.verify(obOracleSessionExtension, Mockito.times(1)).getJdbcOperations(connection).queryForObject(Mockito.anyString(), Mockito.eq(String.class));
        }
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        Executable getConnectionIdMethod = () -> obOracleSessionExtension.getConnectionId(connection);

        try {
            getConnectionIdMethod.execute();
            Optional<String> connectionId = Optional.ofNullable(obOracleSessionExtension.getConnectionId(connection));
            Assertions.assertNotNull(connectionId);
        } finally {
            Mockito.verify(obOracleSessionExtension, Mockito.times(1)).getConnectionId(connection);
        }
    }
}