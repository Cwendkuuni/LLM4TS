import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.mockito.Mockito;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "connection_1";
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.killQuery(connection, connectionId);
        Mockito.verify(connection).createStatement();
        Mockito.verify(connection).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, schemaName);
        Mockito.verify(connection).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String currentSchema = "default_schema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(currentSchema);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String actualSchema = extension.getCurrentSchema(connection);
        assertEquals(currentSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String actualConnectionId = extension.getConnectionId(connection);
        assertEquals(null, actualConnectionId);

        // Test with OceanBaseConnection
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn("thread_id_1");
        connection = oceanBaseConnection;
        actualConnectionId = extension.getConnectionId(connection);
        assertEquals("thread_id_1", actualConnectionId);
    }
}