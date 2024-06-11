import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.pf4j.Extension;
import org.pf4j.TestExtension;
import org.pf4j.TestPluginWrapper;
import org.pf4j.util.Pair;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.*;

@TestExtension
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.killQuery(connection, "connectionId");
        verify(connection).createStatement();
        verify(connection).execute("KILL QUERY connectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String schemaName = "testSchema";
        extension.switchSchema(connection, schemaName);
        verify(connection).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String currentSchema = extension.getCurrentSchema(connection);
        Assertions.assertNotEmpty(currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String connectionId = extension.getConnectionId(connection);
        Assertions.assertNotNull(connectionId);
    }

    @Test
    public void testKillQuery_NullConnection() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Assertions.assertThrows(NullPointerException.class, () -> extension.killQuery(null, "connectionId"));
    }

    @Test
    public void testKillQuery_EmptyConnectionId() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Assertions.assertThrows(IllegalArgumentException.class, () -> extension.killQuery(connection, ""));
    }

    @Test
    public void testSwitchSchema_NullConnection() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Assertions.assertThrows(NullPointerException.class, () -> extension.switchSchema(null, "schemaName"));
    }

    @Test
    public void testSwitchSchema_EmptySchemaName() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Assertions.assertThrows(IllegalArgumentException.class, () -> extension.switchSchema(connection, ""));
    }

    @Test
    public void testSwitchSchema_AlreadyOnSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        when(connection.getCatalog()).thenReturn(schemaName);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, schemaName);
        verifyNoInteractions(connection);
    }

    @Test
    public void testGetCurrentSchema_NullConnection() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Assertions.assertThrows(NullPointerException.class, () -> extension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionId_NullConnection() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Assertions.assertThrows(NullPointerException.class, () -> extension.getConnectionId(null));
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() throws SQLException {
        Connection connection = mock(OceanBaseConnection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String connectionId = extension.getConnectionId(connection);
        Assertions.assertNotNull(connectionId);
    }

    @Test
    public void testGetConnectionId_Exception() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        when(connection.createStatement()).thenThrow(SQLException.class);
        Assertions.assertNull(extension.getConnectionId(connection));
    }

    @Test
    public void testGetCurrentSchema_EmptyResultSet() throws SQLException {
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String currentSchema = extension.getCurrentSchema(connection);
        Assertions.assertEquals("", currentSchema);
    }

    @Test
    public void testGetConnectionId_SingleResult() throws SQLException {
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString(1)).thenReturn("testSchema");
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String currentSchema = extension.getCurrentSchema(connection);
        Assertions.assertEquals("testSchema", currentSchema);
    }
}