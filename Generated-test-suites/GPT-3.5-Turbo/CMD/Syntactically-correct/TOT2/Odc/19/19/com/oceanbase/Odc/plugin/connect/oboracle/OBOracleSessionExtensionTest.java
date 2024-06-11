import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        obOracleSessionExtension.killQuery(connection, connectionId);

        // Assert that the kill query statement was executed on the connection
        verify(connection, times(1)).execute("KILL QUERY 12345");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "TEST_SCHEMA";

        // Test when the current schema is different from the target schema
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("OTHER_SCHEMA");

        obOracleSessionExtension.switchSchema(connection, schemaName);

        // Assert that the alter session SQL was executed on the connection
        verify(connection, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA='TEST_SCHEMA'");

        // Test when the current schema is equal to the target schema
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("TEST_SCHEMA");

        obOracleSessionExtension.switchSchema(connection, schemaName);

        // Assert that no SQL was executed on the connection
        verify(connection, times(0)).update(anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);

        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("TEST_SCHEMA");

        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);

        assertEquals("TEST_SCHEMA", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);

        // Test when query for connection ID is successful
        when(obOracleSessionExtension.getConnectionId(connection)).thenReturn("12345");

        String connectionId = obOracleSessionExtension.getConnectionId(connection);

        assertEquals("12345", connectionId);

        // Test when an exception is thrown and connection is an instance of OceanBaseConnection
        when(obOracleSessionExtension.getConnectionId(connection)).thenThrow(new SQLException());
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(67890);

        connectionId = obOracleSessionExtension.getConnectionId(connection);

        assertEquals("67890", connectionId);
    }
}