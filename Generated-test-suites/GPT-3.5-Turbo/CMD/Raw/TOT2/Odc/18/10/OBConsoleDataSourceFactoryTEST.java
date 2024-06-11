import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setUp() {
        // Initialize the OBConsoleDataSourceFactory with dummy values for testing
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("username");
        connectionConfig.setPassword("password");
        connectionConfig.setDefaultSchema("schema");
        
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/schema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false";
        String actualJdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
        assertEquals(expectedJdbcUrl, actualJdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String expectedUsername = "username";
        String actualUsername = OBConsoleDataSourceFactory.getUsername(obConsoleDataSourceFactory.getConnectionConfig());
        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    public void testGetPassword() {
        String expectedPassword = "password";
        String actualPassword = OBConsoleDataSourceFactory.getPassword(obConsoleDataSourceFactory.getConnectionConfig());
        assertEquals(expectedPassword, actualPassword);
    }

    @Test
    public void testGetDefaultSchema() {
        String expectedDefaultSchema = "schema";
        String actualDefaultSchema = OBConsoleDataSourceFactory.getDefaultSchema(obConsoleDataSourceFactory.getConnectionConfig());
        assertEquals(expectedDefaultSchema, actualDefaultSchema);
    }

}