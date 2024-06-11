
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.SQLException;

import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.SessionExtensionPoint;

@RunWith(PowerMockRunner.class)
public class OBOracleSessionExtensionTest {
    private static final String TEST_SCHEMA = "testSchema";
    private static final String TEST_CONNECTION_ID = "123456";

    @org.junit.Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @org.junit.Test
    public void testKillQuery() throws Exception {
        Connection connection = PowerMockito.mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();

        PowerMockito.whenNew(Connection.class).withArguments(SessionExtensionPoint.class).thenReturn(connection);
        PowerMockito.when(connection.createStatement()).thenReturn(PowerMockito.mock(java.sql.Statement.class));
        PowerMockito.doCallRealMethod().when(connection).createStatement();

        extension.killQuery(connection, "testConnectionId");
    }

    @org.junit.Test
    public void testSwitchSchema() throws Exception {
        Connection connection = PowerMockito.mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();

        PowerMockito.whenNew(Connection.class).withArguments(SessionExtensionPoint.class).thenReturn(connection);

        PowerMockito.when(connection.createStatement()).thenReturn(PowerMockito.mock(java.sql.Statement.class));
        PowerMockito.when(connection.createStatement().execute("ALTER SESSION SET CURRENT_SCHEMA=" + TEST_SCHEMA)).thenReturn(true);

        extension.switchSchema(connection, TEST_SCHEMA);
    }

    @org.junit.Test(expected = SQLException.class)
    public void testSwitchSchemaException() throws Exception {
        Connection connection = PowerMockito.mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();

        PowerMockito.whenNew(Connection.class).withArguments(SessionExtensionPoint.class).thenReturn(connection);

        PowerMockito.when(connection.createStatement()).thenThrow(new SQLException("Test exception"));

        extension.switchSchema(connection, "testSchema");
    }

    @org.junit.Test
    public void testGetCurrentSchema() throws Exception {
        Connection connection = PowerMockito.mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();

        PowerMockito.whenNew(Connection.class).withArguments(SessionExtensionPoint.class).thenReturn(connection);

        PowerMockito.when(connection.createStatement()).thenReturn(PowerMockito.mock(java.sql.Statement.class));
        PowerMockito.when(connection.createStatement().executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")).thenReturn(PowerMockito.mock(java.sql.ResultSet.class));
        PowerMockito.when(connection.createStatement().executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL").next()).thenReturn(true);
        PowerMockito.when(connection.createStatement().executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL").getString("CURRENT_SCHEMA")).thenReturn("testSchema");

        assertEquals(TEST_SCHEMA, extension.getCurrentSchema(connection));
    }

    @org.junit.Test(expected = SQLException.class)
    public void testGetCurrentSchemaException() throws Exception {
        Connection connection = PowerMockito.mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();

        PowerMockito.whenNew(Connection.class).withArguments(SessionExtensionPoint.class).thenReturn(connection);

        PowerMockito.when(connection.createStatement()).thenThrow(new SQLException("Test exception"));

        try {
            extension.getCurrentSchema(connection);
            fail();
        } catch (Exception e) {
            assertEquals("Test exception", e.getMessage());
        }
    }

    @org.junit.Test
    public void testGetConnectionId() throws Exception {
        Connection connection = PowerMockito.mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();

        PowerMockito.whenNew(Connection.class).withArguments(SessionExtensionPoint.class).thenReturn(connection);

        PowerMockito.when(connection instanceof OceanBaseConnection).thenReturn(true);

        assertEquals(TEST_CONNECTION_ID, extension.getConnectionId(connection));
    }
}