package com.oceanbase.odc.service.session.factory;

import static com.oceanbase.odc.common.util.StringUtils.isNotBlank;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
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

public class OBConsoleDataSourceFactoryTest {

    private ConnectionExtensionPoint connectionExtensionPoint;
    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory dataSourceFactory;
    private EventPublisher eventPublisher;

    @Before
    public void setUp() {
        connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
        connectionConfig = new ConnectionConfig();
        eventPublisher = Mockito.mock(EventPublisher.class);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String username = dataSourceFactory.getUsername();
        assertEquals(getUsername(connectionConfig), username);
    }

    @Test
    public void testGetPassword() {
        String password = dataSourceFactory.getPassword();
        assertEquals(connectionConfig.getPassword(), password);
    }

    @Test
    public void testGetJdbcParams() {
        Map<String, String> jdbcParams = dataSourceFactory.getJdbcParams(connectionConfig);
        assertNotNull(jdbcParams);
        assertEquals("64000000", jdbcParams.get("maxAllowedPacket"));
        assertEquals("true", jdbcParams.get("allowMultiQueries"));
        assertEquals("5000", jdbcParams.get("connectTimeout"));
        assertEquals(DEFAULT_ZERO_DATE_TIME_BEHAVIOR, jdbcParams.get("zeroDateTimeBehavior"));
        assertNull(jdbcParams.get("sendConnectionAttributes"));

        OBTenantEndpoint endpoint = new OBTenantEndpoint();
        endpoint.setProxyHost("proxyHost");
        endpoint.setProxyPort(1234);
        connectionConfig.setEndpoint(endpoint);
        jdbcParams = dataSourceFactory.getJdbcParams(connectionConfig);
        assertEquals("proxyHost", jdbcParams.get("socksProxyHost"));
        assertEquals("1234", jdbcParams.get("socksProxyPort"));

        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        connectionConfig.setSslConfig(sslConfig);
        jdbcParams = dataSourceFactory.getJdbcParams(connectionConfig);
        assertEquals("true", jdbcParams.get("useSSL"));
        assertEquals("true", jdbcParams.get("trustServerCertificate"));

        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("/path/to/keystore.p12");
        connectionConfig.setSslFileEntry(sslFileEntry);
        jdbcParams = dataSourceFactory.getJdbcParams(connectionConfig);
        assertEquals("/path/to/keystore.p12", jdbcParams.get("trustStore"));
        assertEquals("/path/to/keystore.p12", jdbcParams.get("keyStore"));
        assertEquals("/path/to/keystore.p12", jdbcParams.get("keyStorePassword"));
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = dataSourceFactory.getDialectType();
        assertEquals(connectionConfig.getDialectType(), dialectType);
    }

    @Test
    public void testDeepCopy() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory clone = dataSourceFactory.deepCopy();
        assertEquals(dataSourceFactory.getUsername(), clone.getUsername());
        assertEquals(dataSourceFactory.getPassword(), clone.getPassword());
        assertEquals(dataSourceFactory.getHost(), clone.getHost());
        assertEquals(dataSourceFactory.getPort(), clone.getPort());
        assertEquals(dataSourceFactory.getDefaultSchema(), clone.getDefaultSchema());
        assertEquals(dataSourceFactory.getParameters(), clone.getParameters());
        assertEquals(dataSourceFactory.getConnectionConfig(), clone.getConnectionConfig());
        assertEquals(dataSourceFactory.getAutoCommit(), clone.getAutoCommit());
        assertEquals(dataSourceFactory.getInitConnection(), clone.getInitConnection());
        assertEquals(dataSourceFactory.getEventPublisher(), clone.getEventPublisher());
        assertEquals(dataSourceFactory.getConnectionExtensionPoint(), clone.getConnectionExtensionPoint());
    }

    @Test
    public void testResetProperties() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory dataSourceFactory1 = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = dataSourceFactory1.getDataSource();
        Properties properties = dataSource.getConnectionProperties();
        String username = dataSourceFactory1.getUsername();

        OBConsoleDataSourceFactory dataSourceFactory2 = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSourceFactory2.resetUsername(mapper);
        assertEquals(username, dataSourceFactory2.getUsername());
        assertNotSame(username, dataSourceFactory2.getUsername());

        dataSourceFactory2.resetPassword(mapper);
        assertEquals(connectionConfig.getPassword(), dataSourceFactory2.getPassword());

        dataSourceFactory2.resetHost(mapper);
        assertEquals(connectionConfig.getHost(), dataSourceFactory2.getHost());

        dataSourceFactory2.resetPort(mapper);
        assertEquals(connectionConfig.getPort(), dataSourceFactory2.getPort());

        dataSourceFactory2.resetSchema(mapper);
        assertEquals(connectionConfig.getDefaultSchema(), dataSourceFactory2.getDefaultSchema());

        dataSourceFactory2.resetParameters(mapper);
        Map<String, String> oldParameters = dataSourceFactory1.getParameters();
        Map<String, String> newParameters = dataSourceFactory2.getParameters();
        assertNotSame(oldParameters, newParameters);
        assertFalse(oldParameters.equals(newParameters));
        assertTrue(oldParameters.keySet().containsAll(newParameters.keySet()));
        assertTrue(newParameters.values().containsAll(oldParameters.values()));
    }

    private String getUsername(@NonNull ConnectionConfig connectionConfig) {
        String username = connectionConfig.getUsername();
        if (DialectType.OB_ORACLE.equals(connectionConfig.getDialectType())) {
            username = "\"" + username + "\"";
        }
        if (isNotBlank(connectionConfig.getOBTenantName())) {
            username += "@" + connectionConfig.getOBTenantName();
        } else if (isNotBlank(connectionConfig.getTenantName())) {
            username += "@" + connectionConfig.getTenantName();
        }
        if (isNotBlank(connectionConfig.getClusterName())) {
            username += "#" + connectionConfig.getClusterName();
        }
        return username;
    }
}