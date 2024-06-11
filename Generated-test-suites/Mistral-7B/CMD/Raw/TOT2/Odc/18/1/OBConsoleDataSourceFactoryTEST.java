import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.PooledObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.common.constant.OdcConstants;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.PoolableConnectionWrapper;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

    private static final String TEST_USERNAME = "testUsername";
    private static final String TEST_PASSWORD = "testPassword";
    private static final String TEST_HOST = "testHost";
    private static final int TEST_PORT = 1234;
    private static final String TEST_DEFAULT_SCHEMA = "testSchema";
    private static final String TEST_JDBC_URL = "jdbc:test://" + TEST_HOST + ":" + TEST_PORT + "/" + TEST_DEFAULT_SCHEMA;
    private static final Map<String, String> TEST_JDBC_PARAMS = new HashMap<>();
    private static final ConnectionConfig TEST_CONNECTION_CONFIG = new ConnectionConfig();
    private static final ConnectionInitializer TEST_CONNECTION_INITIALIZER = mock(ConnectionInitializer.class);
    private static final SessionCreatedInitializer TEST_SESSION_CREATED_INITIALIZER = mock(SessionCreatedInitializer.class);
    private PoolableConnectionFactory<PooledObject<Connection>> connectionFactory;
    private DataSourceFactory dataSourceFactory;
    private PoolingDataSource<Connection> dataSource;
    private Connection connection;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        TEST_CONNECTION_CONFIG.setUsername(TEST_USERNAME);
        TEST_CONNECTION_CONFIG.setPassword(TEST_PASSWORD);
        TEST_CONNECTION_CONFIG.setHost(TEST_HOST);
        TEST_CONNECTION_CONFIG.setPort(TEST_PORT);
        TEST_CONNECTION_CONFIG.setDefaultSchema(TEST_DEFAULT_SCHEMA);
        TEST_CONNECTION_CONFIG.setDialectType(DialectType.OB_MYSQL);
        TEST_CONNECTION_CONFIG.setType(ConnectType.OB_MYSQL);
        TEST_CONNECTION_CONFIG.setJdbcUrlParameters(TEST_JDBC_PARAMS);
        TEST_CONNECTION_CONFIG.setSslConfig(new SSLConfig(true, null, null));
        TEST_CONNECTION_CONFIG.setEndpoint(new OBTenantEndpoint("testProxyHost", 12345));

        TEST_JDBC_PARAMS.put("maxAllowedPacket", "64000000");
        TEST_JDBC_PARAMS.put("allowMultiQueries", "true");
        TEST_JDBC_PARAMS.put("connectTimeout", "5000");
        TEST_JDBC_PARAMS.put("zeroDateTimeBehavior", OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR);
        TEST_JDBC_PARAMS.put("noDatetimeStringSync", "true");
        TEST_JDBC_PARAMS.put("jdbcCompliantTruncation", "false");

        when(TEST_CONNECTION_CONFIG.getJdbcUrlParameters()).thenReturn(TEST_JDBC_PARAMS);

        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectionExtensionPoint.getDriverClassName());
        this.connectionFactory = new PoolableConnectionFactory<>(connectionFactory);
        this.dataSourceFactory = new OBConsoleDataSourceFactory(TEST_CONNECTION_CONFIG, false, true);
        this.dataSource = new PoolingDataSource<>();
        this.dataSource.setCloseQuietly(true);
        this.dataSource.setFactory(this.connectionFactory);
        this.dataSource.addInitializer(TEST_CONNECTION_INITIALIZER);
        this.dataSource.addInitializer(TEST_SESSION_CREATED_INITIALIZER);

        this.connection = this.dataSourceFactory.getDataSource().getConnection();
        this.jdbcTemplate = new JdbcTemplate(this.dataSourceFactory.getDataSource());
    }

    @After
    public void tearDown() throws SQLException {
        this.connection.close();
        this.dataSource.destroy();
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = this.dataSourceFactory.getDataSource();
        assertTrue(dataSource instanceof PoolingDataSource);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = this.dataSourceFactory.getJdbcUrl();
        String expectedJdbcUrl = connectionExtensionPoint.generateJdbcUrl(TEST_HOST, TEST_PORT, TEST_DEFAULT_SCHEMA, TEST_JDBC_PARAMS);
        assertEquals(expectedJdbcUrl, jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        assertEquals(TEST_USERNAME, this.dataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals(TEST_PASSWORD, this.dataSourceFactory.getPassword());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.OB_MYSQL, this.dataSourceFactory.getDialectType());
    }

    @Test
    public void testGetDataSource_WithConnectionInitializers() throws SQLException {
        when(TEST_CONNECTION_INITIALIZER.initializeDatabase(any(Connection.class))).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Connection connection = invocation.getArgument(0);
                Statement statement = connection.createStatement();
                statement.execute("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY, name VARCHAR(50))");
                statement.close();
                return null;
            }
        });

        when(TEST_SESSION_CREATED_INITIALIZER.initializeSession(any(ConnectionConfig.class), any(Connection.class))).thenReturn(null);

        Connection connection = this.dataSourceFactory.getDataSource().getConnection();
        this.jdbcTemplate.execute("DROP TABLE IF EXISTS test_table");
        this.dataSourceFactory.getDataSource().getConnection().close();

        this.dataSourceFactory.getDataSource().getConnection();

        verify(TEST_CONNECTION_INITIALIZER, times(1)).initializeDatabase(this.connection);
        verify(TEST_SESSION_CREATED_INITIALIZER, atLeastOnce()).initializeSession(TEST_CONNECTION_CONFIG, connection);
    }

    @Test
    public void testDeepCopy() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;

        OBConsoleDataSourceFactory originalDataSourceFactory = new OBConsoleDataSourceFactory(TEST_CONNECTION_CONFIG, false, true);
        OBConsoleDataSourceFactory clonedDataSourceFactory = originalDataSourceFactory.deepCopy();

        clonedDataSourceFactory.resetUsername(mapper);
        clonedDataSourceFactory.resetPassword(mapper);
        clonedDataSourceFactory.resetHost(mapper);
        clonedDataSourceFactory.resetPort(mapper);
        clonedDataSourceFactory.resetSchema(mapper);
        clonedDataSourceFactory.resetParameters(mapper);

        assertEquals(TEST_USERNAME, originalDataSourceFactory.getUsername());
        assertEquals(TEST_PASSWORD, originalDataSourceFactory.getPassword());
        assertEquals(TEST_HOST, originalDataSourceFactory.getHost());
        assertEquals(TEST_PORT, originalDataSourceFactory.getPort());
        assertEquals(TEST_DEFAULT_SCHEMA, originalDataSourceFactory.getDefaultSchema());
        assertEquals(TEST_JDBC_PARAMS, originalDataSourceFactory.getParameters());

        assertEquals(TEST_USERNAME, clonedDataSourceFactory.getUsername());
        assertEquals(TEST_PASSWORD, clonedDataSourceFactory.getPassword());
        assertEquals(TEST_HOST, clonedDataSourceFactory.getHost());
        assertEquals(TEST_PORT, clonedDataSourceFactory.getPort());
        assertEquals(TEST_DEFAULT_SCHEMA, clonedDataSourceFactory.getDefaultSchema());
        assertEquals(TEST_JDBC_PARAMS, clonedDataSourceFactory.getParameters());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSource_WithNegativePort() {
        TEST_CONNECTION_CONFIG.setPort(-1);
        this.dataSourceFactory = new OBConsoleDataSourceFactory(TEST_CONNECTION_CONFIG, false, true);

        try {
            this.dataSourceFactory.getDataSource();
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Port can not be negative", e.getMessage());
        }
    }
}