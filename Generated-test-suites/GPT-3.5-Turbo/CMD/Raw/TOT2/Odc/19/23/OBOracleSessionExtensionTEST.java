import static org.junit.Assert.*;
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

        // Assert that the expected SQL query was executed on the connection
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("current_schema");

        obOracleSessionExtension.switchSchema(connection, schemaName);

        // Assert that the ALTER SESSION query was executed on the connection
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).update("ALTER SESSION SET CURRENT_SCHEMA='test_schema'");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);

        obOracleSessionExtension.getCurrentSchema(connection);

        // Assert that the expected query was executed to get the current schema
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenThrow(new SQLException());
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(12345);

        String connectionId = obOracleSessionExtension.getConnectionId(connection);

        // Assert that the connection ID was retrieved correctly
        assertEquals("12345", connectionId);
    }
}