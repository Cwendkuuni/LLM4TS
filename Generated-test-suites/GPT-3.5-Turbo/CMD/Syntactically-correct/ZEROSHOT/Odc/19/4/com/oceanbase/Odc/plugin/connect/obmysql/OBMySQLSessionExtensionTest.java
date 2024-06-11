import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import org.junit.Test;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.killQuery(connection, "12345");
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY 12345");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("schema1");
        obMySQLSessionExtension.switchSchema(connection, "schema2");
        verify(connection).setCatalog("schema2");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("schema1");
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals("schema1", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn("12345");
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals("12345", connectionId);
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(98765L);
        String altConnectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals("98765", altConnectionId);
    }

}