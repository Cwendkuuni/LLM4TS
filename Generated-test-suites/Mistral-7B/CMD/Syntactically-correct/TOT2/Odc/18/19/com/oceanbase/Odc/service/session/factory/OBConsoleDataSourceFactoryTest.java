import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        String jdbcUrl = "jdbc:test:ob://localhost:1527/testdb;zeroDateTimeBehavior=" + DEFAULT_ZERO_DATE_TIME_BEHAVIOR;
        Properties properties = new Properties();
        properties.setProperty("user", "testUser");
        properties.setProperty("password", "testPassword");

        dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig("testDialectType", jdbcUrl, properties), true, true);
        connection = DriverManager.getConnection(jdbcUrl, properties);
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testGetDataSource() throws Exception {
        DataSource dataSource = dataSourceFactory.getDataSource();
        Assert.assertNotNull(dataSource);
        Connection testConnection = dataSource.getConnection();
        Assert.assertNotNull(testConnection);
        testConnection.close();
    }

    @Test
    public void testGetJdbcUrl() throws Exception {
        String expectedJdbcUrl = "jdbc:test:ob://localhost:1527/testdb;zeroDateTimeBehavior=" + DEFAULT_ZERO_DATE_TIME_BEHAVIOR +
                "&maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&useSSL=false&trustServerCertificate=false";
        String actualJdbcUrl = dataSourceFactory.getJdbcUrl();
        Assert.assertEquals(expectedJdbcUrl, actualJdbcUrl);
    }

    @Test(expected = SQLException.class)
    public void testGetDataSourceWithInvalidConfig() throws Exception {
        String jdbcUrl = "jdbc:test:ob://localhost:1527/testdb";
        Properties properties = null;

        new OBConsoleDataSourceFactory(new ConnectionConfig("testDialectType", jdbcUrl, properties), true, true);
    }

    @Test(expected = SQLException.class)
    public void testGetDataSourceWithInvalidUsername() throws Exception {
        String jdbcUrl = "jdbc:test:ob://localhost:1527/testdb;zeroDateTimeBehavior=" + DEFAULT_ZERO_DATA_TIME_BEHAVIOR;
        Properties properties = new Properties();
        properties.setProperty("user", "");

        new OBConsoleDataSourceFactory(new ConnectionConfig("testDialectType", jdbcUrl, properties), true, true);
    }

    @Test(expected = SQLException.class)
    public void testGetDataSourceWithInvalidPassword() throws Exception {
        String jdbcUrl = "jdbc:test:ob://localhost:1527/testdb;zeroDateTimeBehavior=" + DEFAULT_ZERO_DATA_TIME_BEHAVIOR;
        Properties properties = new Properties();
        properties.setProperty("password", "");

        new OBConsoleDataSourceFactory(new ConnectionConfig("testDialectType", jdbcUrl, properties), true, true);
    }

    @Test(expected = SQLException.class)
    public void testGetDataSourceWithInvalidPort() throws Exception {
        String jdbcUrl = "jdbc:test:ob://localhost:-1/testdb;zeroDateTimeBehavior=" + DEFAULT_ZERO_DATA_TIME_BEHAVIOR;
        Properties properties = new Properties();
        properties.setProperty("user", "testUser");
        properties.setProperty("password", "testPassword");

        new OBConsoleDataSourceFactory(new ConnectionConfig("testDialectType", jdbcUrl, properties), true, true);
    }

    @Test
    public void testResetUsername() throws Exception {
        String oldUsername = "testUser";
        String newUsername = "newTestUser";

        dataSourceFactory.resetUsername(mock(CloneableDataSourceFactory.ValueMapper.class));

        Assert.assertEquals(newUsername, dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() throws Exception {
        String oldPassword = "testPassword";
        String newPassword = "newTestPassword";

        dataSourceFactory.resetPassword(mock(CloneableDataSourceFactory.ValueMapper.class));

        Assert.assertEquals(newPassword, dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() throws Exception {
        String oldHost = "localhost";
        String newHost = "newHost";

        dataSourceFactory.resetHost(mock(CloneableDataSourceFactory.ValueMapper.class));

        Assert.assertEquals(newHost, dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() throws Exception {
        int oldPort = 1527;
        int newPort = 1528;

        dataSourceFactory.resetPort(mock(CloneableDataSourceFactory.ValueMapper.class));

        Assert.assertEquals(newPort, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() throws Exception {
        String oldSchema = "testdb";
        String newSchema = "newSchema";

        dataSourceFactory.resetSchema(mock(CloneableDataSourceFactory.ValueMapper.class));

        Assert.assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() throws Exception {
        Map<String, String> oldParameters = new HashMap<>();
        Map<String, String> newParameters = new HashMap<>();

        dataSourceFactory.resetParameters(mock(CloneableDataSourceFactory.ValueMapper.class));

        Assert.assertEquals(newParameters, dataSourceFactory.getParameters());
    }
}