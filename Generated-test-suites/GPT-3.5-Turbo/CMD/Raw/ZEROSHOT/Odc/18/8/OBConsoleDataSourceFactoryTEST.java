import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("user");
        connectionConfig.setPassword("password");
        
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedUrl = "jdbc:mysql://localhost:3306/default?zeroDateTimeBehavior=convertToNull";
        assertEquals(expectedUrl, dataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetDataSource() {
        // Mocking the event publisher
        EventPublisher eventPublisher = mock(EventPublisher.class);
        when(eventPublisher.publish(any())).thenReturn(true);
        dataSourceFactory.setEventPublisher(eventPublisher);
        
        // Mocking the connection extension point
        ConnectionExtensionPoint connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
        when(connectionExtensionPoint.generateJdbcUrl(any(), any(), any(), any())).thenReturn("jdbc:mysql://localhost:3306/default?zeroDateTimeBehavior=convertToNull");
        when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.mysql.jdbc.Driver");
        dataSourceFactory.setConnectionExtensionPoint(connectionExtensionPoint);
        
        // Test getting DataSource
        DataSource dataSource = dataSourceFactory.getDataSource();
        
        assertEquals("jdbc:mysql://localhost:3306/default?zeroDateTimeBehavior=convertToNull", dataSource.getUrl());
        assertEquals("user", dataSource.getUsername());
        assertEquals("password", dataSource.getPassword());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.OB_MYSQL, dataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clonedFactory = dataSourceFactory.deepCopy();
        assertEquals(dataSourceFactory.getDialectType(), ((OBConsoleDataSourceFactory) clonedFactory).getDialectType());
        assertEquals(dataSourceFactory.getJdbcUrl(), ((OBConsoleDataSourceFactory) clonedFactory).getJdbcUrl());
    }

    // Add more test methods to cover all functionalities of OBConsoleDataSourceFactory

}