import org.junit.Test;
import static org.junit.Assert.*;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        // Typical use case
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setUsername("root");
        connectionConfig.setPassword("password");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/"; // Expected JDBC URL for MySQL
        assertEquals(expectedJdbcUrl, factory.getJdbcUrl());
        
        // Edge case
        connectionConfig.setPort(5432);
        connectionConfig.setDialectType(DialectType.POSTGRESQL);
        factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        expectedJdbcUrl = "jdbc:postgresql://localhost:5432/"; // Expected JDBC URL for PostgreSQL
        assertEquals(expectedJdbcUrl, factory.getJdbcUrl());
        
        // Error scenario
        connectionConfig.setDialectType(DialectType.ORACLE);
        factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        expectedJdbcUrl = "jdbc:oracle://localhost:1521/"; // Incorrect JDBC URL for Oracle
        assertNotEquals(expectedJdbcUrl, factory.getJdbcUrl());
    }

    @Test
    public void testGetDataSource() {
        // Typical use case
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setUsername("root");
        connectionConfig.setPassword("password");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertNotNull(factory.getDataSource());
        
        // Edge case
        connectionConfig.setPort(5432);
        connectionConfig.setDialectType(DialectType.POSTGRESQL);
        factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertNotNull(factory.getDataSource());
        
        // Error scenario
        connectionConfig.setDialectType(DialectType.ORACLE);
        factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertNotNull(factory.getDataSource());
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertEquals(DialectType.MYSQL, factory.getDialectType());
    }

    // Additional test cases for other methods in OBConsoleDataSourceFactory...

}