import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;
import org.pf4j.impl.DefaultExtensionManager;
import org.pf4j.impl.DefaultPluginWrapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    private static final String JDBC_DRIVER = "com.oceanbase.jdbc.OceanBaseDriver";
    private static final String DB_URL = "jdbc:obnew:127.0.0.1:28811/test_database";
    private static final String USER = "test_user";
    private static final String PASSWORD = "test_password";

    private OBMySQLSessionExtension obMySQLSessionExtension;
    private Connection connection;
    private ExtensionManager extensionManager;
    private PluginWrapper pluginWrapper;

    @Before
    public void setUp() throws Exception {
        Class.forName(JDBC_DRIVER);
        connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        extensionManager = new DefaultExtensionManager();
        pluginWrapper = new DefaultPluginWrapper("com.oceanbase.odc.plugin.connect.obmysql", "OBMySQLSessionExtension");

        extensionManager.addExtension(pluginWrapper);
    }

    @After
    public void tearDown() throws Exception {
        obMySQLSessionExtension = null;
        connection.close();
        extensionManager = null;
        pluginWrapper = null;
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "1234";
        when(connection.createStatement()).thenReturn(mock(Statement.class));

        obMySQLSessionExtension.killQuery(connection, connectionId);

        String querySql = "KILL QUERY " + connectionId;
        Statement statement = Mockito.mock(Statement.class);
        when(statement.execute(any(String.class))).thenReturn(true);
        when(connection.createStatement()).thenReturn(statement);

        assertEquals(true, obMySQLSessionExtension.killQuery(connection, connectionId));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        when(connection.createStatement()).thenReturn(mock(Statement.class));
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Statement statement = Mockito.mock(Statement.class);

        when(statement.executeQuery("SELECT DATABASE()")).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("DATABASE()")).thenReturn("old_schema");

        obMySQLSessionExtension.switchSchema(connection, schemaName);

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeUpdate("SET SCHEMA " + schemaName)).thenReturn(1);

        assertEquals("old_schema", obMySQLSessionExtension.getCurrentSchema(connection));
        assertEquals(schemaName, obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithExistingSchema() throws SQLException {
        String schemaName = "test_schema";
        when(connection.createStatement()).thenReturn(mock(Statement.class));
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Statement statement = Mockito.mock(Statement.class);

        when(statement.executeQuery("SELECT DATABASE()")).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("DATABASE()")).thenReturn(schemaName);

        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = "current_schema";
        when(connection.createStatement()).thenReturn(mock(Statement.class));
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        when(statement.executeQuery("SELECT DATABASE()")).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("DATABASE()")).thenReturn(currentSchema);

        assertEquals(currentSchema, obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "1234";
        when(connection.createStatement()).thenReturn(mock(Statement.class));
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        when(statement.executeQuery("select connection_id()")).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("CONNECTION_ID()")).thenReturn(connectionId);

        assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithoutQuery() throws SQLException {
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        String connectionId = "5678";
        obMySQLSessionExtension.getConnectionId(connection);
        assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(connection));
    }
}