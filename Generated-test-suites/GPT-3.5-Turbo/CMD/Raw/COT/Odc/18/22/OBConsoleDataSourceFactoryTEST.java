
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        Boolean autoCommit = true;
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, autoCommit);
        
        // When
        String jdbcUrl = factory.getJdbcUrl();
        
        // Then
        assertEquals("jdbc:mysql://localhost:3306/default_schema", jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("admin");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        // When
        String username = factory.getUsername();
        
        // Then
        assertEquals("admin", username);
    }

    @Test
    public void testGetPassword() {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("password123");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        // When
        String password = factory.getPassword();
        
        // Then
        assertEquals("password123", password);
    }

    // Add more test cases for other public methods

}
