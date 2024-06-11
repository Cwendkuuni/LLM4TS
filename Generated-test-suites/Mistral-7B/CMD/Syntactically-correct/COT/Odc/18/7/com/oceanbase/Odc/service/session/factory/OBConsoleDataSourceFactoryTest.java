import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.Validate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;
    @Mock
    private ConnectionSessionUtil connectionSessionUtil;
    @Mock
    private ConnectionPluginUtil connectionPluginUtil;
    @Mock
    private EventPublisher eventPublisher;
    @InjectMocks
    private OBConsoleDataSourceFactory testObject;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(connectionPluginUtil.getConnectionExtension(DialectType.MYSQL)).thenReturn(connectionExtensionPoint);
    }

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setDialectType(DialectType.MYSQL);
        Map<String, String> jdbcParams = new HashMap<>();
        jdbcParams.put("maxAllowedPacket", "64000000");
        connectionConfig.setJdbcUrlParameters(jdbcParams);

        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/testSchema?maxAllowedPacket=64000000";
        String actualJdbcUrl = testObject.getJdbcUrl();

        Assert.assertEquals(expectedJdbcUrl, actualJdbcUrl);
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setType(ConnectType.MYSQL);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
        connectionConfig.setSslConfig(new SSLConfig(true, null, null));

        SingleConnectionDataSource dataSource = testObject.getDataSource();
        Assert.assertNotNull(dataSource);
        Assert.assertEquals(connectionConfig.getDialectType(), dataSource.getDialect());
        Assert.assertEquals("testUser", dataSource.getUsername());
        Assert.assertEquals("testPassword", dataSource.getPassword());
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            Assert.assertNotNull(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceNullConnectionConfig() {
        OBConsoleDataSourceFactory testObject = new OBConsoleDataSourceFactory(null, false);
    }

    @Test
    public void testDeepCopy() {
        ConnectionConfig originalConnectionConfig = new ConnectionConfig();
        originalConnectionConfig.setUsername("testUser");
        originalConnectionConfig.setPassword("testPassword");
        originalConnectionConfig.setHost("localhost");
        originalConnectionConfig.setPort(3306);
        originalConnectionConfig.setDefaultSchema("testSchema");
        originalConnectionConfig.setJdbcUrlParameters(new HashMap<>());
        originalConnectionConfig.setSslConfig(new SSLConfig(true, null, null));
        originalConnectionConfig.setType(ConnectType.MYSQL);

        OBConsoleDataSourceFactory originalTestObject = new OBConsoleDataSourceFactory(originalConnectionConfig, false);
        OBConsoleDataSourceFactory clonedTestObject = testObject.deepCopy(ConnectionMapper.INSTANCE);

        Assert.assertEquals(originalTestObject.getUsername(), clonedTestObject.getUsername());
        Assert.assertEquals(originalTestObject.getPassword(), clonedTestObject.getPassword());
        Assert.assertEquals(originalTestObject.getHost(), clonedTestObject.getHost());
        Assert.assertEquals(originalTestObject.getPort(), clonedTestObject.getPort());
        Assert.assertEquals(originalTestObject.getDefaultSchema(), clonedTestObject.getDefaultSchema());
        Assert.assertEquals(originalTestObject.getJdbcUrl(), clonedTestObject.getJdbcUrl());
        Assert.assertEquals(originalTestObject.getConnectionConfig(), clonedTestObject.getConnectionConfig());
        Assert.assertEquals(originalTestObject.getAutoCommit(), clonedTestObject.getAutoCommit());
        Assert.assertEquals(originalTestObject.getInitConnection(), clonedTestObject.getInitConnection());
    }

    @Test
    public void testResetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUser");
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        testObject.resetUsername(mapper);
        String newUsername = mapper.map("newUsername");
        testObject.setUsername(newUsername);
        Assert.assertEquals(newUsername, testObject.getUsername());
    }

    @Test
    public void testResetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("testPassword");
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        testObject.resetPassword(mapper);
        String newPassword = mapper.map("newPassword");
        testObject.setPassword(newPassword);
        Assert.assertEquals(newPassword, testObject.getPassword());
    }

    @Test
    public void testResetHost() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        testObject.resetHost(mapper);
        String newHost = mapper.map("newHost");
        testObject.setHost(newHost);
        Assert.assertEquals(newHost, testObject.getHost());
    }

    @Test
    public void testResetPort() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPort(3306);
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        testObject.resetPort(mapper);
        Integer newPort = mapper.map(3307);
        testObject.setPort(newPort);
        Assert.assertEquals(newPort, testObject.getPort());
    }

    @Test
    public void testResetSchema() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDefaultSchema("testSchema");
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        testObject.resetSchema(mapper);
        String newSchema = mapper.map("newSchema");
        testObject.setDefaultSchema(newSchema);
        Assert.assertEquals(newSchema, testObject.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("testKey", "testValue");
        testObject.resetParameters(mapper);
        testObject.setJdbcUrlParameters(newParameters);
        Assert.assertEquals(newParameters, testObject.getJdbcUrlParameters());
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUser");
        Assert.assertEquals("testUser", testObject.getUsername());
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("testPassword");
        Assert.assertEquals("testPassword", testObject.getPassword());
    }

    @Test
    public void testGetConnectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setType(ConnectType.MYSQL);
        Assert.assertEquals(ConnectType.MYSQL, testObject.getConnectType());
    }

    @Test
    public void testGetJdbcUrlWithSsl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setSslConfig(new SSLConfig(true, "/path/to/truststore.p12", "/path/to/truststore.p12"));

        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/testSchema?useSSL=true&trustStoreFile=/path/to/truststore.p12&trustStorePassword=truststorepassword";
        String actualJdbcUrl = testObject.getJdbcUrl();

        Assert.assertEquals(expectedJdbcUrl, actualJdbcUrl);
    }
}