import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.pf4j.ExtensionManager;
import org.pf4j.TestExtensionLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection connection;
    private ExtensionManager extensionManager;

    @Before
    public void setUp() throws Exception {
        connection = Mockito.mock(Connection.class);
        extensionManager = new TestExtensionLoader().getExtensionManager();
        obOracleSessionExtension = new OBOracleSessionExtension();
        extensionManager.loadExtensions();
        extensionManager.getExtension(OBOracleSessionExtension.class).init(new Properties());
    }

    @Test
    public void testKillQuery() throws SQLException {
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        obOracleSessionExtension.killQuery(connection, "testConnectionId");
        Mockito.verify(connection.createStatement()).execute("KILL QUERY testConnectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String currentSchema = "testSchema";
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        when(connection.getSchema()).thenReturn(currentSchema);

        obOracleSessionExtension.switchSchema(connection, "newSchemaName");
        Mockito.verify(connection.createStatement()).execute("ALTER SESSION SET CURRENT_SCHEMA=newSchemaName");
        assertEquals("newSchemaName", getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithSameSchema() throws SQLException {
        String currentSchema = "testSchema";
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        when(connection.getSchema()).thenReturn(currentSchema);

        obOracleSessionExtension.switchSchema(connection, currentSchema);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        String currentSchema = "testSchema";
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        when(connection.createStatement().executeQuery(querySql)).thenReturn(Mockito.mock(java.sql.ResultSet.class));
        when(connection.createStatement().executeQuery(querySql).next()).thenReturn(true);
        when(connection.createStatement().executeQuery(querySql).getString(1)).thenReturn(currentSchema);

        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchemaWithNullResultSet() throws SQLException {
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        when(connection.createStatement().executeQuery(querySql)).thenReturn(null);

        assertNull(obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionIdWithUserenv() throws SQLException {
        String querySql = "select userenv('sessionid') from dual";
        String connectionId = "testConnectionId";
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        when(connection.createStatement().executeQuery(querySql)).thenReturn(Mockito.mock(java.sql.ResultSet.class));
        when(connection.createStatement().executeQuery(querySql).next()).thenReturn(true);
        when(connection.createStatement().executeQuery(querySql).getString(1)).thenReturn(connectionId);

        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        String connectionId = "123456";
        when(connection.getClass()).thenReturn(OceanBaseConnection.class);
        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithNullConnection() {
        assertNull(obOracleSessionExtension.getConnectionId(null));
    }

    private String getCurrentSchema(Connection connection) {
        try {
            return obOracleSessionExtension.getCurrentSchema(connection);
        } catch (SQLException e) {
            return null;
        }
    }
}