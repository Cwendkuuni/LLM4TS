import org.junit.Test;

import static org.junit.Assert.*;

public class OBConsoleDataSourceFactoryTest {

    OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Test
    public void testGetJdbcUrl() {
        // Initialize the OBConsoleDataSourceFactory object with a test ConnectionConfig and autoCommit value
        // Call getJdbcUrl() method
        // Assert the returned JDBC URL
        ConnectionConfig testConnectionConfig = new ConnectionConfig();
        testConnectionConfig.setHost("localhost");
        testConnectionConfig.setPort(3306);
        testConnectionConfig.setDialectType(DialectType.MYSQL);
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(testConnectionConfig, true);
        
        assertEquals("jdbc:mysql://localhost:3306/", obConsoleDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetDataSource() {
        // Initialize the OBConsoleDataSourceFactory object with a test ConnectionConfig and autoCommit value
        // Call getDataSource() method
        // Assert that the returned DataSource is not null
        ConnectionConfig testConnectionConfig = new ConnectionConfig();
        testConnectionConfig.setHost("localhost");
        testConnectionConfig.setPort(3306);
        testConnectionConfig.setDialectType(DialectType.MYSQL);
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(testConnectionConfig, true);
        
        assertNotNull(obConsoleDataSourceFactory.getDataSource());
    }

    @Test
    public void testGetDialectType() {
        // Initialize the OBConsoleDataSourceFactory object with a test ConnectionConfig and autoCommit value
        // Call getDialectType() method
        // Assert the returned DialectType
        ConnectionConfig testConnectionConfig = new ConnectionConfig();
        testConnectionConfig.setDialectType(DialectType.MYSQL);
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(testConnectionConfig, true);
        
        assertEquals(DialectType.MYSQL, obConsoleDataSourceFactory.getDialectType());
    }

    // Add more test cases for each public method in the OBConsoleDataSourceFactory class

}