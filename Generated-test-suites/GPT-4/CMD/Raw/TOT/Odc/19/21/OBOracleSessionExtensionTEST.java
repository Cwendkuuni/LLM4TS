import com.oceanbase.jdbc.OceanBaseConnection;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension sessionExtension;
    private Connection connection;
    private OceanBaseConnection oceanBaseConnection;

    @Before
    public void setUp() {
        sessionExtension = new OBOracleSessionExtension();
        connection = Mockito.mock(Connection.class);
        oceanBaseConnection = Mockito.mock(OceanBaseConnection.class);
    }

    @Test
    public void testKillQuery() {
        sessionExtension.killQuery(connection, "testConnectionId");
        // Assuming the success of the method, there is no return so it can't be tested other than it does
        // not throw an exception.
    }

    @Test 
    public void testSwitchSchema_withEqualSchemas() throws SQLException {
        when(connection.getSchema()).thenReturn("schema1");
        sessionExtension.switchSchema(connection, "schema1");
        //No update operation should be performed if schemas are the same
        verify(connection, times(0)).setSchema(anyString());
    }

    @Test 
    public void testSwitchSchema_withDifferentSchemas() throws SQLException {
        when(connection.getSchema()).thenReturn("schema1");
        sessionExtension.switchSchema(connection, "schema2");
        //Update operation should be performed if schemas are the same
        verify(connection, times(1)).setSchema("schema2");
    }

    @Test
    public void testGetCurrentSchema() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(any(String.class), any())).thenReturn("schema1");
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        assertEquals("schema1", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(any(String.class), any())).thenReturn("connection1");
        String connectionId = sessionExtension.getConnectionId(connection);
        assertEquals("connection1", connectionId);
    }

    @Test
    public void testGetConnectionId_withoutSessionId_fromOceanBaseConnection() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), any())).thenThrow(new RuntimeException());
        when(oceanBaseConnection.getServerThreadId()).thenReturn(12345L);

        String connectionId = sessionExtension.getConnectionId(oceanBaseConnection);
        assertEquals("12345", connectionId);
    }
    
    @Test
    public void testGetConnectionId_withoutSessionId_fromNonOceanBaseConnection() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), any())).thenThrow(new RuntimeException());

        try {
            sessionExtension.getConnectionId(connection);
            fail("Expected an Exception to be thrown");
        } catch (Exception e) {
            assertEquals(RuntimeException.class, e.getClass());
        }
    }
}