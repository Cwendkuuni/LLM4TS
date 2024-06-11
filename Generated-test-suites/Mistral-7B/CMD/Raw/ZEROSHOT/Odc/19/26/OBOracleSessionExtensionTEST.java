import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pf4j.ExtensionManager;
import org.pf4j.ExtensionPoint;
import org.pf4j.extensions.annotations.Extension;
import org.pf4j.test.junit.JUnit4JUpgradeExtension;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(JUnit4JUpgradeExtension.class)
public class OBOracleSessionExtensionTest {

    private static final String JDBC_URL = "jdbc:oceanbase:localhost:2880/oceanbase";
    private static final String USER = "user";
    private static final String PASSWORD = "password";

    private OBOracleSessionExtension obOracleSessionExtension;
    private OceanBaseConnection oceanBaseConnection;
    private ExtensionManager extensionManager;

    @Before
    public void setup() throws SQLException, ClassNotFoundException {
        Class.forName("com.oceanbase.jdbc.OceanBaseDriver");
        oceanBaseConnection = (OceanBaseConnection) DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        extensionManager = mock(ExtensionManager.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.setExtensionManager(extensionManager);
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        obOracleSessionExtension.killQuery(connection, "connectionId");
        verify(connection.createStatement()).execute("KILL QUERY connectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connection.getSchema()).thenReturn("currentSchema");

        obOracleSessionExtension.switchSchema(connection, "newSchema");

        verify(connection.createStatement()).execute("ALTER SESSION SET CURRENT_SCHEMA=newSchema");
    }

    @Test
    public void testSwitchSchema_noChange() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connection.getSchema()).thenReturn("newSchema");

        obOracleSessionExtension.switchSchema(connection, "newSchema");

        verify(connection, never()).createStatement().execute("ALTER SESSION SET CURRENT_SCHEMA=newSchema");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String currentSchema = "currentSchema";
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connection.createStatement().executeQuery(anyString())).thenReturn(mock(java.sql.ResultSet.class));
        when(connection.createStatement().executeQuery(anyString())).thenAnswer(invocationOnMock -> {
            Objects.requireNonNull(invocationOnMock.getArgument(0));
            return Optional.of(currentSchema).orElse(null);
        });

        String result = obOracleSessionExtension.getCurrentSchema(connection);

        Assertions.assertEquals("currentSchema", result);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "connectionId";
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));

        String result = obOracleSessionExtension.getConnectionId(connection);

        Assertions.assertEquals("connectionId", result);

        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(42L);

        result = obOracleSessionExtension.getConnectionId(oceanBaseConnection);

        Assertions.assertEquals("42", result);
    }

    @Test
    public void testGetConnectionId_nullConnection() {
        String result = obOracleSessionExtension.getConnectionId(null);

        Assertions.assertNull(result);
    }
}