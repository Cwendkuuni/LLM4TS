import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        // Test case for getJdbcUrl method
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        String expectedUrl = "jdbc:mysql://localhost:3306/defaultSchema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false";
        String actualUrl = factory.getJdbcUrl();
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    public void testGetDataSource() {
        // Test case for getDataSource method
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        // Test case for getDialectType method
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        DialectType expectedDialectType = DialectType.MYSQL;
        DialectType actualDialectType = factory.getDialectType();
        assertEquals(expectedDialectType, actualDialectType);
    }

    // Add more test cases for other public methods

}