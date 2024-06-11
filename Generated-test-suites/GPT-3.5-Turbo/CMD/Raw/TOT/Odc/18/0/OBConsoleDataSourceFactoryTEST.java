import static org.junit.Assert.*;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        // Typical use case
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String jdbcUrl = factory.getJdbcUrl();
        assertEquals("jdbc:mysql://localhost:3306/default_schema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&useSSL=false", jdbcUrl);
    }

    @Test
    public void testGetDataSource() {
        // Edge case
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, false);
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        // Error scenario
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(null);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DialectType dialectType = factory.getDialectType();
        assertNull(dialectType);
    }

    @Test
    public void testDeepCopy() {
        // Error scenario
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        CloneableDataSourceFactory copy = factory.deepCopy();
        assertNotNull(copy);
    }

    @Test
    public void testResetUsername() {
        // Typical use case
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("user1");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetUsername(name -> "newUser");
        assertEquals("newUser", factory.getUsername());
    }

}