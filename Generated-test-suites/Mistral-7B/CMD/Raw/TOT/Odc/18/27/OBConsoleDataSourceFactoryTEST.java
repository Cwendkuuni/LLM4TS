import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.oceanbase.odc.core.shared.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class OBConsoleDataSourceFactoryTest {

    private DataSourceFactory factory;
    private ConnectionExtensionPoint connectionExtensionPoint;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("test-keystore.p12");
        connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testuser");
        connectionConfig.setPassword("testpassword");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
        connectionConfig.setSslConfig(sslConfig);
        connectionConfig.setSslFileEntry(sslFileEntry);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionExtensionPoint = ConnectionPluginUtil.getConnectionExtension(DialectType.OB_MYSQL);
        factory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
    }

    @Test
    public void testGetDataSource() throws SQLException {
        BasicDataSource basicDataSource = (BasicDataSource) factory.getDataSource();
        Connection connection = basicDataSource.getConnection();
        Assert.assertNotNull(connection);
        connection.close();
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = connectionExtensionPoint.generateJdbcUrl(
                "localhost", 3306, "test_schema", factory.getParameters());
        Assert.assertEquals(expectedJdbcUrl, factory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        String expectedUsername = ConnectionSessionUtil.getUserOrSchemaString(
                connectionConfig.getUsername(), DialectType.OB_MYSQL);
        Assert.assertEquals(expectedUsername, factory.getUsername());
    }

    @Test
    public void testGetPassword() {
        Assert.assertEquals(connectionConfig.getPassword(), factory.getPassword());
    }

    @Test
    public void testGetDialectType() {
        Assert.assertEquals(connectionConfig.getDialectType(), factory.getDialectType());
    }

    @Test
    public void testResetUsername() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        factory.resetUsername(mapper);
        Assert.assertEquals(mapper.map(factory.getUsername()), factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        factory.resetPassword(mapper);
        Assert.assertEquals(mapper.map(factory.getPassword()), factory.getPassword());
    }

    @Test
    public void testResetHost() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        factory.resetHost(mapper);
        Assert.assertEquals(mapper.map(factory.getHost()), factory.getHost());
    }

    @Test
    public void testResetPort() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        factory.resetPort(mapper);
        Integer newPort = mapper.map(factory.getPort());
        Assert.assertTrue(newPort > 0);
        Assert.assertEquals(newPort, factory.getPort());
    }

    @Test
    public void testResetSchema() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        factory.resetSchema(mapper);
        Assert.assertEquals(mapper.map(factory.getDefaultSchema()), factory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("test_param", "test_value");
        factory.resetParameters(mapper);
        factory.getParameters().put("test_param", "test_value");
        Assert.assertEquals(newParameters, factory.getParameters());
    }

    @Test
    public void testCloneableDataSourceFactory() throws InstantiationException, IllegalAccessException,
            CloneNotSupportedException {
        OBConsoleDataSourceFactory cloneableDataSourceFactory = factory.deepCopy();
        Assert.assertNotSame(factory, cloneableDataSourceFactory);
        Assert.assertEquals(cloneableDataSourceFactory.getUsername(), factory.getUsername());
        Assert.assertEquals(cloneableDataSourceFactory.getPassword(), factory.getPassword());
        Assert.assertEquals(cloneableDataSourceFactory.getHost(), factory.getHost());
        Assert.assertEquals(cloneableDataSourceFactory.getPort(), factory.getPort());
        Assert.assertEquals(cloneableDataSourceFactory.getDefaultSchema(), factory.getDefaultSchema());
        Assert.assertEquals(cloneableDataSourceFactory.getParameters(), factory.getParameters());
        Assert.assertEquals(cloneableDataSourceFactory.getEventPublisher(), factory.getEventPublisher());
        Assert.assertEquals(cloneableDataSourceFactory.getConnectionExtensionPoint(), factory
                .getConnectionExtensionPoint());
    }

    @Test
    public void testGetDataSourceNoInitializers() throws SQLException {
        DataSourceFactory dataSourceFactory = mock(OBConsoleDataSourceFactory.class);
        SingleConnectionDataSource singleConnectionDataSource = mock(SingleConnectionDataSource.class);
        Properties properties = new Properties();
        properties.setProperty("url", "jdbc:mysql://localhost:3306/test_schema");
        properties.setProperty("user", "testuser");
        properties.setProperty("password", "testpassword");
        singleConnectionDataSource.setUrl(properties.getProperty("url"));
        singleConnectionDataSource.setUsername(properties.getProperty("user"));
        singleConnectionDataSource.setPassword(properties.getProperty("password"));
        DataSource dataSource = Mockito.mock(DataSource.class);
        Mockito.when(singleConnectionDataSource.getConnection()).thenReturn(dataSource);
        Mockito.when(singleConnectionDataSource.getUsername()).thenReturn("testuser");
        Mockito.when(singleConnectionDataSource.getPassword()).thenReturn("testpassword");
        Mockito.when(singleConnectionDataSource.getUrl()).thenReturn(properties.getProperty("url"));
        Mockito.when(dataSource.getConnection()).thenReturn(Mockito.mock(Connection.class));
        Mockito.when(dataSource.isClosed()).thenReturn(false);
        Mockito.when(dataSource.getConnection()).thenReturn(Mockito.mock(Connection.class));
        Mockito.when(dataSourceFactory.getDataSource()).thenReturn(singleConnectionDataSource);
        ConnectionInitializer connectionInitializer = Mockito.mock(ConnectionInitializer.class);
        Mockito.when(connectionInitializer.initializeDatabase()).thenReturn(true);
        factory = new OBConsoleDataSourceFactory(connectionConfig, false, false);
        factory.addInitializer(connectionInitializer);
        DataSource dataSource1 = factory.getDataSource();
        Assert.assertEquals(dataSource1.getClass(), SingleConnectionDataSource.class);
        Assert.assertFalse(dataSource1.isClosed());
        factory.removeInitializer(connectionInitializer);
        DataSource dataSource2 = factory.getDataSource();
        Assert.assertEquals(dataSource2.getClass(), SingleConnectionDataSource.class);
        Assert.assertFalse(dataSource2.isClosed());
        Mockito.verify(connectionInitializer).initializeDatabase();
        DataSource dataSource3 = dataSourceFactory.getDataSource();
        Assert.assertEquals(dataSource3.getClass(), SingleConnectionDataSource.class);
        Assert.assertFalse(dataSource3.isClosed());
        Mockito.verifyZeroInteractions(connectionInitializer);
    }

    @Test
    public void testGetDataSourceWithInitializers() throws SQLException {
        DataSourceFactory dataSourceFactory = mock(OBConsoleDataSourceFactory.class);
        SingleConnectionDataSource singleConnectionDataSource = mock(SingleConnectionDataSource.class);
        Properties properties = new Properties();
        properties.setProperty("url", "jdbc:mysql://localhost:3306/test_schema");
        properties.setProperty("user", "testuser");
        properties.setProperty("password", "testpassword");
        singleConnectionDataSource.setUrl(properties.getProperty("url"));
        singleConnectionDataSource.setUsername(properties.getProperty("user"));
        singleConnectionDataSource.setPassword(properties.getProperty("password"));
        DataSource dataSource = Mockito.mock(DataSource.class);
        Mockito.when(singleConnectionDataSource.getConnection()).thenReturn(dataSource);
        Mockito.when(singleConnectionDataSource.getUsername()).thenReturn("testuser");
        Mockito.when(singleConnectionDataSource.getPassword()).thenReturn("testpassword");
        Mockito.when(singleConnectionDataSource.getUrl()).thenReturn(properties.getProperty("url"));
        Mockito.when(dataSource.getConnection()).thenReturn(Mockito.mock(Connection.class));
        Mockito.when(dataSource.isClosed()).thenReturn(false);
        Mockito.when(dataSource.getConnection()).thenReturn(Mockito.mock(Connection.class));
        Mockito.when(dataSourceFactory.getDataSource()).thenReturn(singleConnectionDataSource);
        ConnectionInitializer connectionInitializer = Mockito.mock(ConnectionInitializer.class);
        Mockito.when(connectionInitializer.initializeDatabase()).thenReturn(true);
        ConnectionInitializer connectionInitializer2 = Mockito.mock(ConnectionInitializer.class);
        Mockito.when(connectionInitializer2.initializeDatabase()).thenReturn(true);
        factory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        factory.addInitializer(connectionInitializer);
        factory.addInitializer(connectionInitializer2);
        DataSource dataSource1 = factory.getDataSource();
        Assert.assertEquals(dataSource1.getClass(), SingleConnectionDataSource.class);
        Assert.assertFalse(dataSource1.isClosed());
        Mockito.verify(connectionInitializer).initializeDatabase();
        Mockito.verify(connectionInitializer2).initializeDatabase();
        factory.removeInitializer(connectionInitializer2);
        DataSource dataSource2 = factory.getDataSource();
        Assert.assertEquals(dataSource2.getClass(), SingleConnectionDataSource.class);
        Assert.assertFalse(dataSource2.isClosed());
        Mockito.verify(connectionInitializer).initializeDatabase();
        Mockito.verifyZeroInteractions(connectionInitializer2);
    }
}