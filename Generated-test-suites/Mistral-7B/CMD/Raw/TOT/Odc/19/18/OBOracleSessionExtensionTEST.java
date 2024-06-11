import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.pf4j.ExtensionManager;
import org.pf4j.impl.DefaultExtensionPoint;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBOracleSessionExtensionTest {

    private SessionExtensionPoint obOracleSessionExtension;
    private ExtensionManager extensionManager;
    private Connection connection;

    @Before
    public void setup() throws SQLException {
        extensionManager = mock(ExtensionManager.class);
        connection = mock(Connection.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.setExtensionManager(extensionManager);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "123";
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        obOracleSessionExtension.killQuery(connection, connectionId);
        Mockito.verify(connection.createStatement()).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchemaSuccess() throws SQLException {
        String schemaName = "test_schema";
        String currentSchema = "old_schema";
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(currentSchema);

        obOracleSessionExtension.switchSchema(connection, schemaName);

        String actualSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, actualSchema);
        Mockito.verify(connection.createStatement()).execute(Mockito.anyString());
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaFailure() throws SQLException {
        String schemaName = "test_schema";
        when(connection.createStatement()).thenThrow(new SQLException());

        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = "test_schema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(currentSchema);

        String actualSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals(currentSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "123";
        Properties infoProperties = new Properties();
        infoProperties.setProperty("sessionid", connectionId);
        when(connection.getInfoProperties()).thenReturn(infoProperties);

        String actualConnectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals(connectionId, actualConnectionId);

        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(123L);
        actualConnectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals(connectionId, actualConnectionId);
    }
}