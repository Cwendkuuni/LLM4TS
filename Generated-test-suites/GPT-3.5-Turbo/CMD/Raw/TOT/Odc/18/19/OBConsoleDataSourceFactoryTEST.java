import static org.junit.Assert.*;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        // Typical use case
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.MYSQL);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String jdbcUrl = factory.getJdbcUrl();
        assertEquals("jdbc:mysql://localhost:3306/test_schema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false", jdbcUrl);
        
        // Edge case
        connectionConfig.setDefaultSchema("");
        jdbcUrl = factory.getJdbcUrl();
        assertEquals("jdbc:mysql://localhost:3306?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false", jdbcUrl);
    }

    @Test
    public void testGetDataSource() {
        // Error scenario
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = factory.getDataSource();
        assertNull(dataSource.getConnection());
        
        // Edge case
        CloneableDataSourceFactory clonedFactory = factory.deepCopy();
        DataSource clonedDataSource = clonedFactory.getDataSource();
        assertNotNull(clonedDataSource.getConnection());
        assertNotEquals(dataSource, clonedDataSource);
    }

    @Test
    public void testResetUsername() {
        // Error scenario
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetUsername(new CloneableDataSourceFactory.ValueMapper<String>() {
            @Override
            public String map(String value) {
                return null;
            }
        });
        assertNull(factory.getUsername());
        
        // Typical use case
        factory.resetUsername(new CloneableDataSourceFactory.ValueMapper<String>() {
            @Override
            public String map(String value) {
                return "new_username";
            }
        });
        assertEquals("new_username", factory.getUsername());
    }

}