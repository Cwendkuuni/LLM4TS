import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.oceanbase.odc.core.shared.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;
    private ConnectionConfig connectionConfig;
    private EventPublisher eventPublisher;
    private ConnectionExtensionPoint connectionExtensionPoint;

    @Before
    public void setup() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(6306);
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);

        eventPublisher = mock(EventPublisher.class);
        connectionExtensionPoint = mock(ConnectionExtensionPoint.class);

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
    }

    @Test
    public void testGetJdbcUrl() throws Exception {
        String expectedJdbcUrl = "jdbc:ob-mysql://localhost:6306/test_schema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=" + DEFAULT_ZERO_DATE_TIME_BEHAVIOR + "&noDatetimeStringSync=true&jdbcCompliantTruncation=false&trustServerCertificate=false";
        String actualJdbcUrl = dataSourceFactory.getJdbcUrl();
        Assert.assertEquals(expectedJdbcUrl, actualJdbcUrl);
    }

    @Test
    public void testGetDataSource() throws SQLException {
        DataSource dataSource = dataSourceFactory.getDataSource();
        Connection connection = dataSource.getConnection();
        Assert.assertNotNull(connection);
        connection.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullConnectionConfig() {
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(null, true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullAutoCommit() {
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullInitConnection() {
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithEmptyHost() {
        connectionConfig.setHost("");
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNegativePort() {
        connectionConfig.setPort(-1);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullUsername() {
        connectionConfig.setUsername(null);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithEmptyPassword() {
        connectionConfig.setPassword("");
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullDefaultSchema() {
        connectionConfig.setDefaultSchema(null);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullConnectionExtensionPoint() {
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(Collections.emptyList());
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true, eventPublisher, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullEventPublisher() {
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true, null, connectionExtensionPoint);
    }

    @Test
    public void testResetUsername() {
        String newUsername = "new_username";
        dataSourceFactory.resetUsername((value) -> newUsername);
        Assert.assertEquals(newUsername, dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "new_password";
        dataSourceFactory.resetPassword((value) -> newPassword);
        Assert.assertEquals(newPassword, dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "new_host";
        dataSourceFactory.resetHost((value) -> newHost);
        Assert.assertEquals(newHost, dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        int newPort = 1234;
        dataSourceFactory.resetPort((value) -> newPort);
        Assert.assertEquals(newPort, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "new_schema";
        dataSourceFactory.resetSchema((value) -> newSchema);
        Assert.assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("new_param", "new_value");
        dataSourceFactory.resetParameters((value) -> newParameters);
        Assert.assertEquals(newParameters, dataSourceFactory.getParameters());
    }

}