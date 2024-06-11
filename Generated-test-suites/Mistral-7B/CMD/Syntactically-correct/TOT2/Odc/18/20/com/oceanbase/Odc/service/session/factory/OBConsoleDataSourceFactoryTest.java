import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.oceanbase.odc.core.shared.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR;

@RunWith(SpringRunner.class)
public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;
    private BasicDataSource basicDataSource;
    private static final String TEST_DATABASE_URL = "jdbc:test:ob:localhost:1527/testdb";
    private static final String TEST_USERNAME = "test_user";
    private static final String TEST_PASSWORD = "test_password";

    @Before
    public void setup() throws Exception {
        Properties connectionProperties = new Properties();
        connectionProperties.setProperty("maxAllowedPacket", "64000000");
        connectionProperties.setProperty("allowMultiQueries", "true");
        connectionProperties.setProperty("connectTimeout", "5000");
        connectionProperties.setProperty("zeroDateTimeBehavior", DEFAULT_ZERO_DATE_TIME_BEHAVIOR);
        connectionProperties.setProperty("noDatetimeStringSync", "true");
        connectionProperties.setProperty("jdbcCompliantTruncation", "false");

        Map<String, String> parameters = new HashMap<>();
        parameters.put("socksProxyHost", "localhost");
        parameters.put("socksProxyPort", "1080");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername(TEST_USERNAME);
        connectionConfig.setPassword(TEST_PASSWORD);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1527);
        connectionConfig.setDatabase("testdb");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setJdbcUrlParameters(parameters);
        connectionConfig.setSslConfig(new SSLConfig(true, null, null));

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
        basicDataSource = new BasicDataSource();
        BasicDataSourceFactory.setDataSourceFactory(dataSourceFactory);
    }

    @Test
    public void testGetDataSource() throws Exception {
        DataSource dataSource = dataSourceFactory.getDataSource();
        Connection connection = dataSource.getConnection();
        try {
            Assert.assertNotNull(connection);
            Assert.assertTrue(connection.isClosed());
            connection.createStatement().executeQuery("SELECT 1 FROM DUAL");
            connection.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        Assert.assertNotNull(jdbcUrl);
        Assert.assertTrue(jdbcUrl.contains("testdb"));
        Assert.assertTrue(jdbcUrl.contains(TEST_USERNAME));
        Assert.assertTrue(jdbcUrl.contains("test_schema"));
        Assert.assertTrue(jdbcUrl.contains("localhost"));
        Assert.assertTrue(jdbcUrl.contains("1527"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullConnectionConfig() {
        dataSourceFactory = new OBConsoleDataSourceFactory(null, true, false);
    }

    @Test
    public void testGetDataSourceWithEmptyConnectionConfig() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
        Assert.assertNull(dataSourceFactory.getDataSource());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername(null);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword(null);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullHost() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost(null);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullPort() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPort(null);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithEmptyHost() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("");
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNegativePort() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPort(-1);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullDefaultSchema() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDefaultSchema(null);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithEmptyDefaultSchema() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDefaultSchema("");
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullJdbcUrlParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullSslConfig() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        SSLConfig sslConfig = null;
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, false, sslConfig);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullConnectionExtensionPoint() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        ConnectionExtensionPoint connectionExtensionPoint = null;
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, false, connectionExtensionPoint);
    }

    @After
    public void tearDown() throws Exception {
        if (basicDataSource != null) {
            basicDataSource.close();
        }
    }
}