import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection mockConnection = mock(Connection.class);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.killQuery(mockConnection, "123");
        verify(mockConnection).prepareStatement("KILL QUERY 123");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.switchSchema(mockConnection, "test_schema");
        verify(mockConnection).setCatalog("test_schema");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection mockConnection = mock(Connection.class);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("test_schema");
        assertEquals("test_schema", obMySQLSessionExtension.getCurrentSchema(mockConnection));
    }

    @Test
    public void testGetConnectionId() {
        Connection mockConnection = mock(Connection.class);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select connection_id()", String.class)).thenReturn("456");
        assertEquals("456", obMySQLSessionExtension.getConnectionId(mockConnection));
    }
}