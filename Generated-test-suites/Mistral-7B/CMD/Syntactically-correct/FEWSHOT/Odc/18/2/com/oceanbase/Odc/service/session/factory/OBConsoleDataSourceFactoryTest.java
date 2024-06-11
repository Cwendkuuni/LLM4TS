package com.oceanbase.odc.service.session.factory;

import static com.oceanbase.odc.core.shared.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR;
import static org.junit.Assert.*;

import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.plugin.ConnectionExtensionPoint;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionPluginUtil;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

/**
 * Test class for OBConsoleDataSourceFactory.
 *
 * @author yh263208
 * @since ODC_release_3.2.2
 */
public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(6000);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setSslConfig(new SSLConfig(true, null, null));
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        String jdbcUrl = factory.getJdbcUrl();
        assertEquals("jdbc:ob-mysql://localhost:6000/test_schema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior="
                + DEFAULT_ZERO_DATE_TIME_BEHAVIOR + "&noDatetimeStringSync=true&jdbcCompliantTruncation=false&useSSL=true&trustServerCertificate=true", jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("test_user");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        String username = factory.getUsername();
        assertEquals("test_user", username);
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        String password = factory.getPassword();
        assertNotNull(password);
        assertEquals("test_password", password);
    }

    @Test
    public void testGetJdbcParams() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(6000);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        Map<String, String> jdbcUrlParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        assertEquals(12, jdbcUrlParams.size());
        assertTrue(jdbcUrlParams.containsKey("maxAllowedPacket"));
        assertTrue(jdbcUrlParams.containsKey("allowMultiQueries"));
        assertTrue(jdbcUrlParams.containsKey("connectTimeout"));
        assertTrue(jdbcUrlParams.containsKey("zeroDateTimeBehavior"));
        assertTrue(jdbcUrlParams.containsKey("noDatetimeStringSync"));
        assertTrue(jdbcUrlParams.containsKey("jdbcCompliantTruncation"));
        assertTrue(jdbcUrlParams.containsKey("useSSL"));
        assertTrue(jdbcUrlParams.containsKey("trustServerCertificate"));
        SSLConfig sslConfig = new SSLConfig(true, "test_key_store_path", "test_key_store_password");
        connectionConfig.setSslConfig(sslConfig);
        jdbcUrlParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        assertTrue(jdbcUrlParams.containsKey("disableSslHostnameVerification"));
        assertTrue(jdbcUrlParams.containsKey("trustStore"));
        assertTrue(jdbcUrlParams.containsKey("trustStorePassword"));
        assertTrue(jdbcUrlParams.containsKey("keyStore"));
        assertTrue(jdbcUrlParams.containsKey("keyStorePassword"));
        assertTrue(jdbcUrlParams.containsKey("trustServerCertificate"));
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(6000);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
        assertEquals("com.mysql.jdbc.JDBC4Driver", dataSource.getDriverClassName());
        String jdbcUrl = factory.getJdbcUrl();
        Properties connectionProperties = dataSource.getConnectionProperties();
        assertEquals(12, countProperties(connectionProperties));
        assertEquals(jdbcUrl, connectionProperties.getProperty("url"));
        assertEquals("test_user", connectionProperties.getProperty("user"));
        assertEquals("test_password", connectionProperties.getProperty("password"));
        assertEquals("true", connectionProperties.getProperty("useSSL"));
        assertEquals("true", connectionProperties.getProperty("trustServerCertificate"));
        assertTrue(connectionProperties.containsKey("maxAllowedPacket"));
        assertTrue(connectionProperties.containsKey("allowMultiQueries"));
        assertTrue(connectionProperties.containsKey("connectTimeout"));
        assertTrue(connectionProperties.containsKey("zeroDateTimeBehavior"));
        assertTrue(connectionProperties.containsKey("noDatetimeStringSync"));
        assertTrue(connectionProperties.containsKey("jdbcCompliantTruncation"));
        assertTrue(connectionProperties.containsKey("disableSslHostnameVerification"));
        assertTrue(connectionProperties.containsKey("trustStore"));
        assertTrue(connectionProperties.containsKey("trustStorePassword"));
        assertTrue(connectionProperties.containsKey("keyStore"));
        assertTrue(connectionProperties.containsKey("keyStorePassword"));
    }

    @Test
    public void testResetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("test_user");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        factory.resetUsername(new CloneableDataSourceFactory.ValueMapper<String>() {
            @Override
            public String map(String original) {
                return "new_user";
            }
        });
        assertEquals("new_user", factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        factory.resetPassword(new CloneableDataSourceFactory.ValueMapper<String>() {
            @Override
            public String map(String original) {
                return "new_password";
            }
        });
        assertEquals("new_password", factory.getPassword());
    }

    private int countProperties(Properties properties) {
        int count = 0;
        for (Object key : properties.keySet().toArray()) {
            count++;
        }
        return count;
    }
}