import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@RunWith(PowerMockRunner.class)
public class OBConsoleDataSourceFactoryTest {

    private static final String DIALECT_TYPE = "OB_ORACLE";
    private static final String USERNAME = "testUsername";
    private static final String PASSWORD = "testPassword";
    private static final String HOST = "testHost";
    private static final int PORT = 1234;
    private static final String DEFAULT_SCHEMA = "testSchema";
    private static final String JDBC_URL = "jdbc:test://" + HOST + ":" + PORT + "/" + DEFAULT_SCHEMA;
    private ConnectionExtensionPoint connectionExtensionPoint;
    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory dataSourceFactory;
    private SingleConnectionDataSource dataSource;
    private EventPublisher eventPublisher;

    @Before
    public void setup() {
        connectionExtensionPoint = PowerMockito.mock(ConnectionExtensionPoint.class);
        connectionConfig = new ConnectionConfig();
        connectionConfig.setType(ConnectType.OB_ORACLE);
        connectionConfig.setUsername(USERNAME);
        connectionConfig.setPassword(PASSWORD);
        connectionConfig.setHost(HOST);
        connectionConfig.setPort(PORT);
        connectionConfig.setDialectType(DIALECT_TYPE);
        connectionConfig.setDefaultSchema(DEFAULT_SCHEMA);

        eventPublisher = Mockito.mock(EventPublisher.class);

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
    }

    @Test
    public void testGetDataSource() throws SQLException {
        Assert.assertNotNull(dataSource);
        Assert.assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        Assert.assertEquals(JDBC_URL, jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String username = dataSourceFactory.getUsername();
        Assert.assertEquals(USERNAME, username);
    }

    @Test
    public void testGetPassword() {
        String password = dataSourceFactory.getPassword();
        Assert.assertEquals(PASSWORD, password);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = dataSourceFactory.getDialectType();
        Assert.assertEquals(DIALECT_TYPE, dialectType);
    }

    @Test
    public void testGetDataSourceWithConnectionInitializers() throws SQLException {
        List<ConnectionInitializer> connectionInitializers = Mockito.mockList(ConnectionInitializer.class);
        PowerMockito.when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(connectionInitializers);

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();

        // Verify that connection initializers are called
        Mockito.verify(connectionInitializers.get(0), Mockito.times(1)).initializeDatabase(dataSource.getConnection());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullConnectionConfig() {
        dataSourceFactory = new OBConsoleDataSourceFactory(null, false, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullConnectionExtensionPoint() {
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        PowerMockito.whenNew(ConnectionPluginUtil.class).withArguments(DIALECT_TYPE).thenThrow(new RuntimeException());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithInvalidConnectionConfig() {
        connectionConfig.setPort(-1);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory originalDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        OBConsoleDataSourceFactory clonedDataSourceFactory = originalDataSourceFactory.deepCopy();

        Assert.assertNotSame(originalDataSourceFactory.getPassword(), clonedDataSourceFactory.getPassword());
    }

    @Test
    public void testResetUsername() {
        String newUsername = "newUsername";
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        PowerMockito.when(mapper.map(USERNAME)).thenReturn(newUsername);

        dataSourceFactory.resetUsername(mapper);
        Assert.assertEquals(newUsername, dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "newPassword";
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        PowerMockito.when(mapper.map(PASSWORD)).thenReturn(newPassword);

        dataSourceFactory.resetPassword(mapper);
        Assert.assertEquals(newPassword, dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "newHost";
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        PowerMockito.when(mapper.map(HOST)).thenReturn(newHost);

        dataSourceFactory.resetHost(mapper);
        Assert.assertEquals(newHost, dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        int newPort = 5678;
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        PowerMockito.when(mapper.map(PORT)).thenReturn(newPort);

        dataSourceFactory.resetPort(mapper);
        Assert.assertEquals(newPort, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "newSchema";
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        PowerMockito.when(mapper.map(DEFAULT_SCHEMA)).thenReturn(newSchema);

        dataSourceFactory.resetSchema(mapper);
        Assert.assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        Map<String, String> oldParameters = dataSourceFactory.getParameters();
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);

        PowerMockito.when(mapper.map(oldParameters)).thenReturn(newParameters);

        dataSourceFactory.resetParameters(mapper);
        Assert.assertEquals(newParameters, dataSourceFactory.getParameters());
    }
}