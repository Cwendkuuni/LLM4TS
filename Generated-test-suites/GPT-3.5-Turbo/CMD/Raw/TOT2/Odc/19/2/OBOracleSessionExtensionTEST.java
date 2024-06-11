
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        obOracleSessionExtension.killQuery(connection, "123");
        // Add assertion here
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        // Test when current schema is equal to schema name
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("schema");
        obOracleSessionExtension.switchSchema(connection, "schema");
        // Add assertion here
        
        // Test when current schema is not equal to schema name
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("other_schema");
        obOracleSessionExtension.switchSchema(connection, "new_schema");
        // Add assertion here
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        // Test when current schema is retrieved successfully
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("schema");
        assertEquals("schema", obOracleSessionExtension.getCurrentSchema(connection));
        
        // Test when current schema is not retrieved successfully
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenThrow(new SQLException());
        assertNull(obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        // Test when connection is not an instance of OceanBaseConnection
        String sessionId = "123";
        when(obOracleSessionExtension.getConnectionId(connection)).thenReturn(sessionId);
        assertEquals(sessionId, obOracleSessionExtension.getConnectionId(connection));

        // Test when connection is an instance of OceanBaseConnection
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        String threadId = "456";
        when(oceanBaseConnection.getServerThreadId()).thenReturn(threadId);
        when(obOracleSessionExtension.getConnectionId(oceanBaseConnection)).thenReturn(threadId);
        assertEquals(threadId, obOracleSessionExtension.getConnectionId(oceanBaseConnection));
    }
}
