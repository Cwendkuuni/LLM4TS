import static org.junit.Assert.*;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        // Typical use case
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1521);
        connectionConfig.setDefaultSchema("TEST");
        connectionConfig.setUsername("user");
        connectionConfig.setPassword("password");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String jdbcUrl = factory.getJdbcUrl();
        assertEquals("jdbc:oracle:thin:@//localhost:1521/TEST", jdbcUrl);

        // Edge case
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        jdbcUrl = factory.getJdbcUrl();
        assertEquals("jdbc:mysql://localhost:1521/TEST", jdbcUrl);

        // Error scenario
        connectionConfig.setHost("");
        try {
            factory.getJdbcUrl();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetDataSource() {
        // Typical use case
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1521);
        connectionConfig.setDefaultSchema("TEST");
        connectionConfig.setUsername("user");
        connectionConfig.setPassword("password");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);

        // Edge case
        factory.setEventPublisher(new EventPublisher());
        dataSource = factory.getDataSource();
        assertNotNull(dataSource);

        // Error scenario
        connectionConfig.setHost("");
        try {
            factory.getDataSource();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetDialectType() {
        // Typical use case
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DialectType dialectType = factory.getDialectType();
        assertEquals(DialectType.OB_ORACLE, dialectType);

        // Edge case
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        dialectType = factory.getDialectType();
        assertEquals(DialectType.OB_MYSQL, dialectType);

        // Error scenario
        connectionConfig.setDialectType(null);
        try {
            factory.getDialectType();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    // Add more test cases for other methods as needed

}