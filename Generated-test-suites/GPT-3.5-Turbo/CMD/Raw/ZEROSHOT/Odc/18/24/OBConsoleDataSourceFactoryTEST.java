import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {
    
    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory dataSourceFactory;
    
    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setDefaultSchema("testSchema");
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }
    
    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/testSchema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&useSSL=false&allowLoadLocalInfile=false&allowUrlInLocalInfile=false&allowLoadLocalInfileInPath=&autoDeserialize=false";
        String actualJdbcUrl = dataSourceFactory.getJdbcUrl();
        assertEquals(expectedJdbcUrl, actualJdbcUrl);
    }
    
    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }
    
    @Test
    public void testGetDialectType() {
        DialectType expectedDialectType = DialectType.MYSQL;
        DialectType actualDialectType = dataSourceFactory.getDialectType();
        assertEquals(expectedDialectType, actualDialectType);
    }
    
    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
        assertNotNull(clonedDataSourceFactory);
    }
}