import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import javax.sql.DataSource;

public class OBConsoleDataSourceFactoryTest {

    @Mock private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetJdbcUrl() {
        // Typical use case
        Mockito.when(connectionConfig.getHost()).thenReturn("localhost");
        Mockito.when(connectionConfig.getPort()).thenReturn(3306);
        Mockito.when(connectionConfig.getDefaultSchema()).thenReturn("test_schema");
        Mockito.when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/test_schema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&useSSL=false";
        String actualJdbcUrl = dataSourceFactory.getJdbcUrl();
        assertEquals(expectedJdbcUrl, actualJdbcUrl);

        // Edge case
        Mockito.when(connectionConfig.getHost()).thenReturn("127.0.0.1");
        Mockito.when(connectionConfig.getPort()).thenReturn(5432);
        Mockito.when(connectionConfig.getDefaultSchema()).thenReturn("public");
        Mockito.when(connectionConfig.getDialectType()).thenReturn(DialectType.PG);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
        expectedJdbcUrl = "jdbc:postgresql://127.0.0.1:5432/public?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&useSSL=false";
        actualJdbcUrl = dataSourceFactory.getJdbcUrl();
        assertEquals(expectedJdbcUrl, actualJdbcUrl);
    }

    // Add more test cases for other methods

}