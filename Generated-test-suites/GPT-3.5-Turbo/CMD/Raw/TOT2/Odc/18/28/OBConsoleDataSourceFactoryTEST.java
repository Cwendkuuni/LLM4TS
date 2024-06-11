import static org.junit.Assert.*;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        // Test getJdbcUrl method in OBConsoleDataSourceFactory
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/testdb?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false";
        String actualJdbcUrl = factory.getJdbcUrl();
        assertEquals(expectedJdbcUrl, actualJdbcUrl);
    }

    @Test
    public void testGetUsername() {
        // Test getUsername method in OBConsoleDataSourceFactory
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setUsername("root");
        connectionConfig.setOBTenantName("ob_tenant");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String expectedUsername = "root@ob_tenant";
        String actualUsername = factory.getUsername();
        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    public void testGetPassword() {
        // Test getPassword method in OBConsoleDataSourceFactory
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("password123");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String expectedPassword = "password123";
        String actualPassword = factory.getPassword();
        assertEquals(expectedPassword, actualPassword);
    }

    // Add more test cases for other methods in OBConsoleDataSourceFactory

}