import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory obFactory;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setDefaultSchema("testSchema");

        obFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedUrl = "jdbc:oracle:thin:@localhost:3306/testSchema";
        assertEquals(expectedUrl, obFactory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        String expectedUsername = "testUser";
        assertEquals(expectedUsername, obFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        String expectedPassword = "testPassword";
        assertEquals(expectedPassword, obFactory.getPassword());
    }

    // Add more test cases for other methods and edge cases

}