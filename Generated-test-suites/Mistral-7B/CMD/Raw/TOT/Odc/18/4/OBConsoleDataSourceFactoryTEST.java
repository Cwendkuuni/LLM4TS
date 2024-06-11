import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.DefaultDbcp2BasicDataSourceFactory;
import org.apache.commons.dbcp2.util.BasicPoolProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;
    private ConnectionConfig connectionConfig;
    private EventPublisher eventPublisher;
    private ConnectionExtensionPoint connectionExtensionPoint;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
        eventPublisher = mock(EventPublisher.class);
        connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
    }

    @After
    public void tearDown() throws SQLException {
        if (dataSourceFactory != null) {
            if (dataSourceFactory.getDataSource() != null) {
                dataSourceFactory.getDataSource().close();
            }
        }
    }

    @Test
    public void testGetDataSource() {
        // Test case 1: Basic test with valid configuration
        Properties dbProperties = new Properties();
        dbProperties.setProperty("driverClassName", "com.ob.driver.MyDriver");
        dbProperties.setProperty("url", "jdbc:ob://localhost:1527/testdb");
        dbProperties.setProperty("username", "testuser");
        dbProperties.setProperty("password", "testpassword");

        when(connectionConfig.getJdbcUrlParameters()).thenReturn(dbProperties);
        when(connectionExtensionPoint.generateJdbcUrl(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyMap()))
                .thenReturn("jdbc:ob://localhost:1527/testdb");

        Connection connection = Mockito.mock(Connection.class);
        ConnectionManager connectionManager = Mockito.mock(ConnectionManager.class);
        when(connectionManager.getConnection()).thenReturn(connection);
        when(connectionExtensionPoint.getConnectionManager()).thenReturn(connectionManager);

        DataSource dataSource = dataSourceFactory.getDataSource();

        assertEquals("com.ob.driver.MyDriver", dataSource.getDriverClassName());
        assertEquals("jdbc:ob://localhost:1527/testdb", dataSource.getUrl());
        assertEquals("testuser", dataSource.getUsername());
        assertEquals("testpassword", dataSource.getPassword());

        // Test case 2: Test with null configuration
        dataSourceFactory = new OBConsoleDataSourceFactory(null, true, true);

        assertEquals(null, dataSourceFactory.getDataSource());

        // Test case 3: Test with invalid configuration (empty username)
        connectionConfig.setUsername(StringUtils.EMPTY);

        assertEquals(null, dataSourceFactory.getDataSource());

        // Test case 4: Test with invalid configuration (empty password)
        connectionConfig.setPassword(StringUtils.EMPTY);

        assertEquals(null, dataSourceFactory.getDataSource());

        // Test case 5: Test with invalid configuration (null host)
        connectionConfig.setHost(null);

        assertEquals(null, dataSourceFactory.getDataSource());

        // Test case 6: Test with invalid configuration (negative port)
        connectionConfig.setPort(-1);

        Validate.isTrue(dataSourceFactory.getDataSource() == null, "DataSource should be null with invalid configuration");
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.OB_ORACLE, dataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory originalFactory = dataSourceFactory;
        OBConsoleDataSourceFactory clonedFactory = (OBConsoleDataSourceFactory) originalFactory.deepCopy();

        assertEquals(originalFactory.getUsername(), clonedFactory.getUsername());
        assertEquals(originalFactory.getPassword(), clonedFactory.getPassword());
        assertEquals(originalFactory.getHost(), clonedFactory.getHost());
        assertEquals(originalFactory.getPort(), clonedFactory.getPort());
        assertEquals(originalFactory.getConnectionConfig(), clonedFactory.getConnectionConfig());
    }
}