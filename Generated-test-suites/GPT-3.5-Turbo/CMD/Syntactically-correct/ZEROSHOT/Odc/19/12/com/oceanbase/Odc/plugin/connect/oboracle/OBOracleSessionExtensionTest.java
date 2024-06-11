import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
    private Connection mockConnection = mock(Connection.class);

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "123";
        obOracleSessionExtension.killQuery(mockConnection, connectionId);
        // Verify that the execute method is called with the correct query
        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        when(obOracleSessionExtension.getCurrentSchema(mockConnection)).thenReturn("oldSchema");
        obOracleSessionExtension.switchSchema(mockConnection, schemaName);
        // Verify that the update method is called with the correct query
        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection)).update("ALTER SESSION SET CURRENT_SCHEMA='" + schemaName + "'");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String expectedSchema = "testSchema";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(anyString(), eq(String.class))).thenReturn(expectedSchema);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(mockConnection);
        assertEquals(expectedSchema, currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String expectedConnectionId = "456";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(anyString(), eq(String.class)))
                .thenThrow(new SQLException("Error"))
                .thenReturn(expectedConnectionId);
        String connectionId = obOracleSessionExtension.getConnectionId(mockConnection);
        assertEquals(expectedConnectionId, connectionId);

        // Test case for when connection is an instance of OceanBaseConnection
        OceanBaseConnection mockOceanBaseConnection = mock(OceanBaseConnection.class);
        when(obOracleSessionExtension.getConnectionId(mockOceanBaseConnection)).thenReturn("789");
        String oceanBaseConnectionId = obOracleSessionExtension.getConnectionId(mockOceanBaseConnection);
        assertEquals("789", oceanBaseConnectionId);
    }
}