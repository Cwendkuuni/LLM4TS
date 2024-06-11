import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory factory;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = new ConnectionConfig("localhost", 3306, "username", "password", DialectType.MYSQL);
        factory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/defaultSchema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&useSSL=false";
        String actualJdbcUrl = factory.getJdbcUrl();
        assertEquals(expectedJdbcUrl, actualJdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String expectedUsername = "\"username\"";
        String actualUsername = OBConsoleDataSourceFactory.getUsername(factory.connectionConfig);
        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    public void testGetPassword() {
        String expectedPassword = "password";
        String actualPassword = OBConsoleDataSourceFactory.getPassword(factory.connectionConfig);
        assertEquals(expectedPassword, actualPassword);
    }

    @Test
    public void testGetJdbcParams() {
        Map<String, String> expectedJdbcParams = new HashMap<>();
        expectedJdbcParams.put("maxAllowedPacket", "64000000");
        expectedJdbcParams.put("allowMultiQueries", "true");
        expectedJdbcParams.put("connectTimeout", "5000");
        expectedJdbcParams.put("zeroDateTimeBehavior", "convertToNull");
        expectedJdbcParams.put("noDatetimeStringSync", "true");
        expectedJdbcParams.put("jdbcCompliantTruncation", "false");
        expectedJdbcParams.put("useSSL", "false");

        Map<String, String> actualJdbcParams = OBConsoleDataSourceFactory.getJdbcParams(factory.connectionConfig);
        assertEquals(expectedJdbcParams, actualJdbcParams);
    }

    // Add more test cases here...

}