import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;
    
    // Initializing test data
    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPass");
        connectionConfig.setDialectType(DialectType.MYSQL);
        
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }
    
    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        // Add additional assertions for the DataSource object if needed
    }
    
    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/test?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&useSSL=false";
        assertEquals(expectedJdbcUrl, dataSourceFactory.getJdbcUrl());
    }

    // TODO: Add more test cases for other methods in OBConsoleDataSourceFactory
    
    // Edge cases
    @Test
    public void testGetJdbcUrlWithSslEnabled() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        // Set SSL enabled to true
        connectionConfig.setSslConfig(new SSLConfig(true));
        OBConsoleDataSourceFactory sslEnabledDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        String expectedSslEnabledJdbcUrl = "jdbc:mysql://localhost:3306/test?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&useSSL=true&trustServerCertificate=true";
        
        assertEquals(expectedSslEnabledJdbcUrl, sslEnabledDataSourceFactory.getJdbcUrl());
    }

    // Error scenarios
    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullConnectionConfig() {
        OBConsoleDataSourceFactory nullConfigDataSourceFactory = new OBConsoleDataSourceFactory(null, true);
        nullConfigDataSourceFactory.getDataSource(); // This should throw an IllegalArgumentException
    }
    
    // Mocking data for more specific scenarios
    @Test
    public void testMockedDataSource() {
        DataSource mockDataSource = mock(DataSource.class);
        when(mockDataSource.getConnection()).thenReturn(null); // Mocking getConnection() method
        
        // Using the mocked DataSource in the OBConsoleDataSourceFactory
        OBConsoleDataSourceFactory mockedDataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true) {
            @Override
            public DataSource getDataSource() {
                return mockDataSource;
            }
        };
        
        DataSource dataSource = mockedDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        // Verify any interactions with the mockDataSource if needed
        verify(mockDataSource).getConnection();
    }
}