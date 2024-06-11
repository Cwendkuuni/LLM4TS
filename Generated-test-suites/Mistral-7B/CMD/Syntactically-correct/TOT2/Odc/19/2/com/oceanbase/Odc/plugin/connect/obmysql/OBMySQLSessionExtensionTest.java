import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.powermock;
import static org.powermock.api.mockito.PowerMockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({OBMySQLSessionExtension.class})
public class OBMySQLSessionExtensionTest {
    @Test
    public void testKillQuery() {
        Connection connection = Mockito.mock(Connection.class);
        String connectionId = "12345";
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.killQuery(connection, connectionId);
        assertNoExceptionThrown(extension::killQuery, connection, connectionId);
    }

    @Test
    public void testKillQueryWithInvalidConnection() {
        Connection connection = Mockito.mock(Connection.class);
        PowerMockito.when(connection.isClosed()).thenReturn(true);
        String connectionId = "12345";
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        assertThrows(IllegalStateException.class, () -> extension.killQuery(connection, connectionId));
    }

    @Test
    public void testKillQueryWithInvalidConnectionId() {
        Connection connection = Mockito.mock(Connection.class);
        String connectionId = null;
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        assertThrows(NullPointerException.class, () -> extension.killQuery(connection, connectionId));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        String schemaName = "testSchema";
        String currentSchema = "defaultSchema";
        PowerMockito.when(OBMySQLSessionExtension.this.getCurrentSchema(connection)).thenReturn(currentSchema);
        PowerMockito.when(connection.setCatalog(schemaName)).thenReturn(true);
        PowerMockito.when(OBMySQLSessionExtension.this.getCurrentSchema(connection)).thenReturn(schemaName);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, schemaName);

        assertEquals(schemaName, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        PowerMockito.when(OBMySQLSessionExtension.this.queryForObject(connection, "SELECT DATABASE()")).thenReturn(schemaName);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        assertEquals(schemaName, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";
        PowerMockito.when(connection.isClosed()).thenReturn(false);
        PowerMockito.when(connection instanceof OceanBaseConnection).thenReturn(true);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        assertEquals(connectionId, extension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdFromNullConnection() {
        Connection connection = null;
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        assertEquals(null, extension.getConnectionId(connection));
    }
}