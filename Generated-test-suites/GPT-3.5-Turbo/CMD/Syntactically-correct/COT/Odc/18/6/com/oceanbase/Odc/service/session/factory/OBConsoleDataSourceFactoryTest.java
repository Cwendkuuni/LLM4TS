
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Test
    public void testGetJdbcUrl() {
        // Test functionality of getJdbcUrl method
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDialectType(DialectType.MYSQL);
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/";
        assertEquals(expectedJdbcUrl, obConsoleDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        // Test functionality of getUsername method
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("user1");
        connectionConfig.setDialectType(DialectType.MYSQL);
        String expectedUsername = "user1";
        assertEquals(expectedUsername, OBConsoleDataSourceFactory.getUsername(connectionConfig));
    }

    @Test
    public void testGetPassword() {
        // Test functionality of getPassword method
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("password123");
        String expectedPassword = "password123";
        assertEquals(expectedPassword, OBConsoleDataSourceFactory.getPassword(connectionConfig));
    }

    // Add more test cases for other public methods in OBConsoleDataSourceFactory class

}
