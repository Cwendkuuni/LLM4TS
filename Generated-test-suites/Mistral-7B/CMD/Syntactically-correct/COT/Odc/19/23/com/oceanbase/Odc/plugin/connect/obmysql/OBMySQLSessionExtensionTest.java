import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.impl.DefaultExtensionManager;
import org.pf4j.plugins.annnotation. RafikExtension;
import org.pf4j.plugins.annnotation.PF4JExtension;
import org.powermock.api.mockito.staticmock.PowerMockStatic;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@PrepareForTest({OBMySQLSessionExtension.class, OceanBaseConnection.class})
@SuppressWarnings("unchecked")
public class OBMySQLSessionExtensionTest {

    private static Extension extension;
    private static Connection connection;

    @Before
    public void setUp() {
        ExtensionManager extensionManager = new DefaultExtensionManager();
        extension = extensionManager.getExtensionByClass(OBMySQLSessionExtension.class);
        connection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        extension.killQuery(connection, "connectionId");
        PowerMockStatic.verifyStatic();
        java.sql.Statement statement = PowerMockStatic.statement("KILL QUERY connectionId");
        PowerMockStatic.mockStatic(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(statement);
        PowerMockStatic.mockStatic(connection.getClass());
        PowerMockStatic.doNothing().when(connection, "setCatalog", "schemaName");
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() throws SQLException {
        when(connection.createStatement()).thenThrow(new SQLException("Test Exception"));
        extension.killQuery(connection, "connectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        String currentSchema = "oldSchema";
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        extension.switchSchema(connection, schemaName);
        PowerMockStatic.verifyStatic();
        java.sql.Statement statement = PowerMockStatic.statement("SET SCHEMA testSchema");
        PowerMockStatic.mockStatic(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(statement);
        PowerMockStatic.mockStatic(connection.getClass());
        when(connection.getCatalog()).thenReturn(currentSchema);
        when(connection.setCatalog("testSchema")).thenReturn(true);
        PowerMockStatic.mockStatic(Objects.class);
        when(Objects.equals(currentSchema, schemaName)).thenReturn(false);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        String schemaName = "testSchema";
        when(connection.createStatement()).thenThrow(new SQLException("Test Exception"));
        extension.switchSchema(connection, schemaName);
    }

    @Test
    public void testgetCurrentSchema() throws SQLException {
        String schemaName = "testSchema";
        String expectedSchema = "expectedSchema";
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockStatic.anyString(), PowerMockStatic.eq(String.class))).thenReturn(schemaName);
        assertEquals(expectedSchema, extension.getCurrentSchema(connection));
    }

    @Test
    public void testgetConnectionId() throws SQLException {
        String connectionId = "testConnectionId";
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockStatic.anyString(), PowerMockStatic.eq(String.class))).thenReturn(connectionId);
        assertEquals(connectionId, extension.getConnectionId(connection));
    }

    @Test
    public void testgetConnectionIdWhenQueryFailed() throws SQLException {
        when(connection.createStatement()).thenThrow(new SQLException("Test Exception"));
        assertEquals("", extension.getConnectionId(connection));
    }

    @Test
    public void testgetConnectionIdWhenConnectionIsNull() {
        assertNull(extension.getConnectionId(null));
    }
}