import org.junit.Test;

import static org.junit.Assert.*;

public class OBConsoleDataSourceFactoryTest {

    OBConsoleDataSourceFactory factory;

    @Test
    public void testGetJdbcUrl() {
        // Typical use case
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1521);
        connectionConfig.setTenantName("test_tenant");
        
        factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String jdbcUrl = factory.getJdbcUrl();
        
        assertEquals("jdbc:oracle:thin:@localhost:1521:test_tenant", jdbcUrl);
    }

    @Test
    public void testGetDataSource() {
       // Edge case
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setHost("127.0.0.1");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("test");
        connectionConfig.setPassword("test123");

        factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = factory.getDataSource();

        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        // Error scenario
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(null);

        factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DialectType dialectType = factory.getDialectType();

        assertNull(dialectType);
    }

    @Test
    public void testDeepCopy() {
        // Typical use case
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setHost("127.0.0.1");
        connectionConfig.setPort(3306);

        factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        CloneableDataSourceFactory copiedFactory = factory.deepCopy();

        assertNotSame(factory, copiedFactory);
    }

}