import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pf4j.Extension;
import org.pf4j.Extensions;
import org.pf4j.impl.DefaultExtensionPoint;
import org.powermock.api.mockito.annotation.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(PowerMockRunner.class)
@PrepareForTest({OBMySQLSessionExtension.class})
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = mock(Connection.class);
        String connectionId = "test_connection_id";
        extension.killQuery(connection, connectionId);
        verify(connection, times(1)).createStatement();
        verify(connection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testKillQuery_WhenConnectionIsNull() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String connectionId = "test_connection_id";
        Assertions.assertThrows(NullPointerException.class, () -> extension.killQuery(null, connectionId));
    }

    @Test
    public void testKillQuery_WhenConnectionIdIsNull() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = mock(Connection.class);
        Assertions.assertDoesNotThrow(() -> extension.killQuery(connection, null));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        extension.switchSchema(connection, schemaName);
        verify(connection, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testSwitchSchema_WhenCurrentSchemaEqualsTargetSchema() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        String currentSchema = "test_schema";
        when(extension.getCurrentSchema(connection)).thenReturn(currentSchema);
        extension.switchSchema(connection, schemaName);
        verify(connection, never()).setCatalog(schemaName);
    }

    @Test
    public void testSwitchSchema_WhenCatalogIsNull() throws SQLException {
        DefaultExtensionPoint extensionPoint = mock(DefaultExtensionPoint.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        extension.switchSchema(connection, schemaName);
        verify(extensionPoint, never()).fireExtensionEvent(extension, "switchSchema");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = mock(Connection.class);
        String expectedSchema = "test_schema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(eq("SELECT DATABASE()"), eq(String.class))).thenReturn(expectedSchema);
        String actualSchema = extension.getCurrentSchema(connection);
        Assertions.assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = mock(Connection.class);
        String expectedConnectionId = "test_connection_id";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(eq("select connection_id()"), eq(String.class))).thenReturn(expectedConnectionId);
        String actualConnectionId = extension.getConnectionId(connection);
        Assertions.assertEquals(expectedConnectionId, actualConnectionId);
    }

    @Test
    public void testGetConnectionId_WhenQueryForObjectThrowsException() throws SQLException {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = mock(Connection.class);
        String expectedConnectionId = null;
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(eq("select connection_id()"), eq(String.class))).thenThrow(new SQLException());
        String actualConnectionId = extension.getConnectionId(connection);
        Assertions.assertNull(actualConnectionId);
    }

    @Test
    public void testGetConnectionId_WhenConnectionIsNull() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String expectedConnectionId = null;
        String actualConnectionId = extension.getConnectionId(null);
        Assertions.assertEquals(expectedConnectionId, actualConnectionId);
    }

    @Test
    public void testGetConnectionId_WhenConnectionIsInstanceOfOceanBaseConnection() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = mock(OceanBaseConnection.class);
        String expectedConnectionId = "test_connection_id";
        when(connection.getServerThreadId()).thenReturn(expectedConnectionId);
        String actualConnectionId = extension.getConnectionId(connection);
        Assertions.assertEquals(expectedConnectionId, actualConnectionId);
    }

    @Test
    public void testGetConnectionId_WhenConnectionIsNotInstanceOfOceanBaseConnection() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = mock(Connection.class);
        String actualConnectionId = extension.getConnectionId(connection);
        Assertions.assertNull(actualConnectionId);
    }

    @org.junit.Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
}