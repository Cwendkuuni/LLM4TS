
import org.junit.Test;
import static org.junit.Assert.*;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        // Test basic functionality
        ConnectionConfig connectionConfig = new ConnectionConfig(); // Create a valid connectionConfig
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String expectedJdbcUrl = "jdbc:dbType://localhost:3306/database";
        assertEquals(expectedJdbcUrl, factory.getJdbcUrl());
    }

    @Test
    public void testNullConnectionConfigConstructor() {
        // Test exception handling for null ConnectionConfig
        try {
            OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(null, true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testEmptyUsernameAndPassword() {
        // Test edge case for empty username and password
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("");
        connectionConfig.setPassword("");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertEquals("", factory.getUsername());
        assertEquals("", factory.getPassword());
    }

    // Add more test cases for other methods and scenarios

}
