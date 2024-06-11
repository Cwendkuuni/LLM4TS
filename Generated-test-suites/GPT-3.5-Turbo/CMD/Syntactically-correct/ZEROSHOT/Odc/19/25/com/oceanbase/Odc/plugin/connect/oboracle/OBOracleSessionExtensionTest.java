import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        obOracleSessionExtension.killQuery(connection, connectionId);

        verify(connection, times(1)).prepareStatement("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";

        obOracleSessionExtension.switchSchema(connection, schemaName);

        verify(connection, times(1)).prepareStatement("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        String expectedSchema = "test_schema";
        when(connection.prepareStatement("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")).thenReturn(mock(PreparedStatement.class));
        when(resultSet.getString(1)).thenReturn(expectedSchema);

        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);

        assertNotNull(currentSchema);
        assertEquals(expectedSchema, currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        String expectedConnectionId = "12345";
        when(connection.prepareStatement("select userenv('sessionid') from dual")).thenReturn(mock(PreparedStatement.class));
        when(resultSet.getString(1)).thenReturn(expectedConnectionId);

        String connectionId = obOracleSessionExtension.getConnectionId(connection);

        assertNotNull(connectionId);
        assertEquals(expectedConnectionId, connectionId);
    }

    @Test
    public void testGetConnectionId_Exception() {
        Connection connection = mock(Connection.class);
        when(connection.prepareStatement("select userenv('sessionid') from dual")).thenThrow(new SQLException());
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(12345);

        String connectionId = obOracleSessionExtension.getConnectionId(connection);

        assertNotNull(connectionId);
        assertEquals("12345", connectionId);
    }
}