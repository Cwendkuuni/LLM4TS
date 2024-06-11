package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.exception.OdcRuntimeException;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_PASSWORD = "testPassword";
    private static final String TEST_HOST = "localhost";
    private static final int TEST_PORT = 63306;
    private static final String TEST_DEFAULT_SCHEMA = "testSchema";
    private static final String TEST_JDBC_URL = "jdbc:test:ds";

    private ConnectionExtensionPoint connectionExtensionPoint;
    private DataSourceFactory dataSourceFactory;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername(TEST_USERNAME);
        connectionConfig.setPassword(TEST_PASSWORD);
        connectionConfig.setHost(TEST_HOST);
        connectionConfig.setPort(TEST_PORT);
        connectionConfig.setDefaultSchema(TEST_DEFAULT_SCHEMA);
        connectionConfig.setType(ConnectType.MYSQL);
        connectionConfig.setDialectType(DialectType.MYSQL);

        this.connectionExtensionPoint = ConnectionPluginUtil.getConnectionExtension(DialectType.MYSQL);
        this.dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
    }

    @After
    public void tearDown() {
        if (this.dataSourceFactory != null) {
            this.dataSourceFactory = null;
        }
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = this.dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        assertNotNull(dataSource.getConnection());
        try {
            Connection connection = dataSource.getConnection();
            assertNotNull(connection);
            connection.close();
        } catch (SQLException e) {
            // ignore
        }
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = this.dataSourceFactory.getJdbcUrl();
        String expectedJdbcUrl = this.connectionExtensionPoint.generateJdbcUrl(TEST_HOST, TEST_PORT, TEST_DEFAULT_SCHEMA, this.dataSourceFactory.getParameters());
        assertTrue(StringUtils.equals(jdbcUrl, expectedJdbcUrl));
    }

    @Test(expected = OdcRuntimeException.class)
    public void testInvalidConnectionConfig() {
        ConnectionConfig invalidConnectionConfig = new ConnectionConfig();
        invalidConnectionConfig.setUsername(TEST_USERNAME);
        invalidConnectionConfig.setPassword(null);
        invalidConnectionConfig.setHost(TEST_HOST);
        invalidConnectionConfig.setPort(TEST_PORT);

        OBConsoleDataSourceFactory invalidDataSourceFactory = new OBConsoleDataSourceFactory(invalidConnectionConfig, true, true);
        assertNull(invalidDataSourceFactory.getDataSource());
    }

    @Test
    public void testJdbcParams() {
        Map<String, String> jdbcUrlParams = this.dataSourceFactory.getParameters();
        assertEquals(10, jdbcUrlParams.size());
        assertTrue(jdbcUrlParams.containsKey("maxAllowedPacket"));
        assertTrue(jdbcUrlParams.containsKey("allowMultiQueries"));
        assertTrue(jdbcUrlParams.containsKey("connectTimeout"));
        assertTrue(jdbcUrlParams.containsKey("zeroDateTimeBehavior"));
        assertTrue(jdbcUrlParams.containsKey("noDatetimeStringSync"));
        assertTrue(jdbcUrlParams.containsKey("jdbcCompliantTruncation"));
        assertTrue(jdbcUrlParams.containsKey("disabledSslHostnameVerification"));
        assertTrue(jdbcUrlParams.containsKey("trustServerCertificate"));
        assertTrue(jdbcUrlParams.containsKey("trustStore"));
        assertTrue(jdbcUrlParams.containsKey("trustStorePassword"));
    }

    @Test
    public void testInitConnection() {
        ConnectionInitializer initializer = this.connectionExtensionPoint.getConnectionInitializers().get(0);
        SingleConnectionDataSource dataSource = (SingleConnectionDataSource) this.dataSourceFactory.getDataSource();
        dataSource.addInitializer(initializer);
        dataSource.initDatabase();
        Connection connection = dataSource.getConnection();
        connection.close();
        assertFalse(dataSource.isClosed());
    }

    @Test
    public void testAutoCommit() {
        DataSource dataSource = this.dataSourceFactory.getDataSource();
        boolean autoCommit = dataSource.getAutoCommit();
        assertTrue(autoCommit);
        dataSource.setAutoCommit(false);
        assertFalse(dataSource.getAutoCommit());
    }

    @Test
    public void testDeepCopy() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory deepCopyDataSourceFactory = (OBConsoleDataSourceFactory) this.dataSourceFactory.deepCopy();
        deepCopyDataSourceFactory.resetUsername(mapper);
        deepCopyDataSourceFactory.resetPassword(mapper);
        deepSourceFactoryTest(deepCopyDataSourceFactory);
    }

    private void deepSourceFactoryTest(DataSourceFactory deepCopyDataSourceFactory) {
        String jdbcUrl = deepCopyDataSourceFactory.getJdbcUrl();
        String username = deepCopyDataSourceFactory.getUsername();
        String password = deepCopyDataSourceFactory.getPassword();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            connection.close();
        } catch (SQLException e) {
            // ignore
        }
        assertNotNull(connection);
    }

    @Test
    public void testResetValues() {
        OBConsoleDataSourceFactory dataSourceFactory = this.dataSourceFactory;
        this.dataSourceFactory = null;
        OBConsoleDataSourceFactory clone = dataSourceFactory.deepCopy();
        clone.resetUsername(value -> TEST_USERNAME);
        clone.resetPassword(value -> TEST_PASSWORD);
        clone.resetHost(value -> TEST_HOST);
        clone.resetPort(value -> TEST_PORT);
        clone.resetSchema(value -> TEST_DEFAULT_SCHEMA);
        clone.resetParameters(value -> new HashMap<String, String>() {{
            put("maxAllowedPacket", "123");
            put("connectTimeout", "456");
        }});
        DataSource cloneDataSource = clone.getDataSource();
        String jdbcUrl = cloneDataSource.getURL();
        assertTrue(jdbcUrl.startsWith(TEST_JDBC_URL));
        assertTrue(TEST_USERNAME.equals(cloneDataSource.getUser()));
        assertTrue(TEST_PASSWORD.equals(cloneDataSource.getPassword()));
        assertNotNull(cloneDataSource.getConnection());
    }

}