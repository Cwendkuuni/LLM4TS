import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        // Given
        Connection connection = mock(Connection.class);
        String connectionId = "123";

        // When
        new OBMySQLSessionExtension().killQuery(connection, connectionId);

        // Then
        // Verify that the execute method is called with the correct SQL query
        verify(connection, times(1)).prepareStatement("KILL QUERY 123");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        // Given
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";

        // When
        new OBMySQLSessionExtension().switchSchema(connection, schemaName);

        // Then
        // Verify that the setCatalog method is called with the correct schemaName
        verify(connection, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        // Given
        Connection connection = mock(Connection.class);

        // When
        String currentSchema = new OBMySQLSessionExtension().getCurrentSchema(connection);

        // Then
        assertNotNull(currentSchema);
        assertTrue(currentSchema instanceof String);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        // Given
        Connection connection = mock(Connection.class);

        // When
        String connectionId = new OBMySQLSessionExtension().getConnectionId(connection);

        // Then
        assertNotNull(connectionId);
        assertTrue(connectionId instanceof String);
    }
    
    // Add more test cases for edge cases and exceptions if needed 
}