package com.oceanbase.odc.plugin.connect.obmysql.test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pf4j.TestExtension;
import org.mockito.Mockito;

@ExtendWith(TestExtension.class)
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "test_connection_id";
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.killQuery(connection, connectionId);
        Mockito.verify(connection).createStatement();
        Mockito.verify(connection).execute("KILL QUERY " + connectionId);
        Assertions.assertDoesNotThrow(() -> extension.killQuery(connection, connectionId));
    }

    // Test cases for edge cases and exception handling scenarios for killQuery()

    @Test
    public void testKillQueryWithNullConnection() {
        Connection connection = null;
        String connectionId = "test_connection_id";
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Assertions.assertThrows(NullPointerException.class, () -> extension.killQuery(connection, connectionId));
    }

    @Test
    public void testKillQueryWithEmptyConnectionId() {
        String connectionId = "";
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Assertions.assertThrows(NullPointerException.class, () -> extension.killQuery(connection, connectionId));
    }

    @Test
    public void testKillQueryWithException() throws SQLException {
        String connectionId = "test_connection_id";
        Connection connection = Mockito.mock(Connection.class);
        Mockito.when(connection.createStatement()).thenThrow(new SQLException("Test Exception"));
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Assertions.assertThrows(SQLException.class, () -> extension.killQuery(connection, connectionId));
    }

    @Test
    public void testSwitchSchemaToSameSchema() throws SQLException {
        String schemaName = "test_schema";
        Connection connection = Mockito.mock(Connection.class);
        String currentSchema = "test_schema";
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, schemaName);
        Mockito.verify(connection).setCatalog(schemaName);
        Mockito.verifyNoMoreInteractions(connection);
    }

    @Test
    public void testSwitchSchemaToNonExistentSchema() throws SQLException {
        String schemaName = "non_existent_schema";
        Connection connection = Mockito.mock(Connection.class);
        String currentSchema = "test_schema";
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Assertions.assertThrows(SQLException.class, () -> extension.switchSchema(connection, schemaName));
    }

    @Test
    public void testSwitchSchemaWithException() throws SQLException {
        String schemaName = "test_schema";
        Connection connection = Mockito.mock(Connection.class);
        String currentSchema = "test_schema";
        Mockito.when(connection.createStatement()).thenThrow(new SQLException("Test Exception"));
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Assertions.assertThrows(SQLException.class, () -> extension.switchSchema(connection, schemaName));
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        String currentSchema = "test_schema";
        Mockito.when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        Mockito.when(connection.queryForObject("SELECT DATABASE()", String.class)).thenReturn(currentSchema);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Assertions.assertEquals(currentSchema, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        String connectionId = "test_connection_id";
        Mockito.when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        Mockito.when(connection.queryForObject("select connection_id()", String.class)).thenReturn(connectionId);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Assertions.assertEquals(connectionId, extension.getConnectionId(connection));
    }
}