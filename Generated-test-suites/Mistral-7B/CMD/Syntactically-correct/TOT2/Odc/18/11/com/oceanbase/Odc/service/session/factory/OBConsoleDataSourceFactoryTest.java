import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.util.DBUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;
    @Mock
    private EventPublisher eventPublisher;
    @InjectMocks
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetDataSource() throws SQLException {
        String jdbcUrl = "jdbc:url:port/schema";
        String username = "testUser";
        String password = "testPassword";
        Map<String, String> parameters = new HashMap<>();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1234);
        connectionConfig.setUsername(username);
        connectionConfig.setPassword(password);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setSslConfig(new SSLConfig(false, null, null));

        when(connectionExtensionPoint.generateJdbcUrl(eq("localhost"), eq(1234), eq("testSchema"), anyMap()))
                .thenReturn(jdbcUrl);

        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();

        assertEquals(username, dataSource.getUsername());
        assertEquals(password, dataSource.getPassword());
        assertEquals(jdbcUrl, dataSource.getConnectionProperties().getProperty("url"));
        assertNotNull(dataSource.getConnection());

        try (Connection connection = dataSource.getConnection()) {
            assertEquals(username, connection.getSchema());
            assertEquals(username + "@testSchema", connection.getCatalog());
            assertTrue(connection.isValid(5000));
            DBUtils.closeQuietly(connection);
        }

        verify(connectionExtensionPoint, times(1)).generateJdbcUrl(eq("localhost"), eq(1234), eq("testSchema"), anyMap());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithInvalidDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        obConsoleDataSourceFactory.getDataSource();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullConnectionConfig() {
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(null, true, true);
    }

    @After
    public void tearDown() throws Exception {
        if (obConsoleDataSourceFactory != null) {
            obConsoleDataSourceFactory.getDataSource().close();
        }
    }
}