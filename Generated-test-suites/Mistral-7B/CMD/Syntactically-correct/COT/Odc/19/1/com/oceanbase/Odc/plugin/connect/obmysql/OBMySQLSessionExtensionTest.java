import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.pf4j.Extension;
import org.pf4j.ExtensionFactory;
import org.pf4j.TestExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(TestExtension.class)
public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension;
    private Connection connection;

    @BeforeEach
    public void setup() {
        connection = mock(Connection.class);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() throws SQLException {
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        obMySQLSessionExtension.killQuery(connection, "connectionId");
        Assertions.assertDoesNotThrow(() -> JdbcOperationsUtil.getJdbcOperations(connection).execute("KILL QUERY connectionId"), "Query should be killed");
    }

    @Test
    public void testSwitchSchema_SameSchema() throws SQLException {
        obMySQLSessionExtension.switchSchema(connection, "testSchema");
        Assertions.assertDoesNotThrow(() -> obMySQLSessionExtension.switchSchema(connection, "testSchema"), "Should not throw exception when switching to the same schema");
    }

    @Test
    public void testSwitchSchema_DifferentSchema() throws SQLException {
        String currentSchema = "testSchema1";
        when(connection.getCatalog()).thenReturn(currentSchema);
        obMySQLSessionExtension.switchSchema(connection, "testSchema2");
        Assertions.assertEquals("testSchema2", connection.getCatalog(), "Schema should be switched to 'testSchema2'");
    }

    @Test
    public void testSwitchSchema_NullSchemaName() {
        Assertions.assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.switchSchema(connection, null), "Should throw exception when schema name is null");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        when(connection.createStatement().executeQuery("SELECT DATABASE()")).thenReturn(Mockito.mock(java.sql.ResultSet.class));
        when(connection.createStatement().next()).thenReturn(true);
        when(connection.createStatement().next()).thenReturn(false);
        Optional<String> schemaName = obMySQLSessionExtension.getCurrentSchema(connection);
        Assertions.assertTrue(schemaName.isPresent(), "Should return schema name");
        Assertions.assertEquals("testSchema", schemaName.get(), "Schema name should be 'testSchema'");
    }

    @Test
    public void testGetCurrentSchema_NoResultSet() throws SQLException {
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        when(connection.createStatement().executeQuery("SELECT DATABASE()")).thenReturn(null);
        Assertions.assertNull(obMySQLSessionExtension.getCurrentSchema(connection), "Should return null when no result set is returned");
    }

    @Test
    public void testGetConnectionId_SqlException() throws SQLException {
        when(connection.createStatement()).thenThrow(new SQLException("Mock SQLException"));
        Assertions.assertThrows(SQLException.class, () -> obMySQLSessionExtension.getConnectionId(connection), "Should throw SQLException when query for connection id fails");
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() {
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        Assertions.assertEquals("12345", obMySQLSessionExtension.getConnectionId(connection), "Connection id should be '12345' for OceanBaseConnection");
    }

    @Test
    public void testGetConnectionId_NullConnection() {
        Assertions.assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.getConnectionId(null), "Should throw exception when connection is null");
    }
}