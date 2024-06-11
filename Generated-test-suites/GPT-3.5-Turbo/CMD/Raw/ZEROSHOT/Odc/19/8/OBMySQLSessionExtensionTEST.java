import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import org.junit.Before;
import org.junit.Test;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension;
    private Connection mockConnection;

    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        mockConnection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "1";
        obMySQLSessionExtension.killQuery(mockConnection, connectionId);
        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection)).execute("KILL QUERY 1");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        when(obMySQLSessionExtension.getCurrentSchema(mockConnection)).thenReturn("other_schema");

        obMySQLSessionExtension.switchSchema(mockConnection, schemaName);
        verify(mockConnection).setCatalog(schemaName);

        when(obMySQLSessionExtension.getCurrentSchema(mockConnection)).thenReturn(schemaName);
        obMySQLSessionExtension.switchSchema(mockConnection, schemaName);
        verify(mockConnection).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn("test_schema");

        String currentSchema = obMySQLSessionExtension.getCurrentSchema(mockConnection);
        assertEquals("test_schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select connection_id()", String.class))
            .thenReturn("1");

        String connectionId = obMySQLSessionExtension.getConnectionId(mockConnection);
        assertEquals("1", connectionId);

        // test for OceanBaseConnection instance
        Connection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(((OceanBaseConnection) oceanBaseConnection).getServerThreadId()).thenReturn(2L);
        String oceanBaseConnectionId = obMySQLSessionExtension.getConnectionId(oceanBaseConnection);
        assertEquals("2", oceanBaseConnectionId);
    }
}


