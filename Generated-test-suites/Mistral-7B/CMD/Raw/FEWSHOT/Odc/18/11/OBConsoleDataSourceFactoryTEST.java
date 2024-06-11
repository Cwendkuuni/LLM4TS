package com.oceanbase.odc.service.session.factory;

import static com.oceanbase.odc.core.shared.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
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

/**
 * Tests for the OBConsoleDataSourceFactory class
 */
public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testOBConsoleDataSourceFactory() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(63306);
        connectionConfig.setUsername("user");
        connectionConfig.setPassword("password");
        connectionConfig.setDefaultSchema("schema");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setSslConfig(new SSLConfig(true, new SSLFileEntry("keyStoreFilePath", "keyStoreFilePassword")));
        connectionConfig.setType(ConnectType.OB_ORACLE);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertNotNull(factory.getDataSource());
        assertEquals(DialectType.OB_ORACLE, factory.getDialectType());

        String jdbcUrl = factory.getJdbcUrl();
        assertNotNull(jdbcUrl);
        assertEquals("jdbc:oceanbase:oracle:th://localhost:63306/user@localhost:63306/schema", jdbcUrl);

        ConnectionInitializer initializer = ConnectionPluginUtil.getConnectionInitializers().get(0);
        factory.getDataSource().addInitializer(initializer);
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("user");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setTenantName("tenant");
        connectionConfig.setClusterName("cluster");

        String username = OBConsoleDataSourceFactory.getUsername(connectionConfig);
        assertEquals("user@tenant#cluster", username);

        connectionConfig.setObTenantName("tenant2");
        username = OBConsoleDataSourceFactory.getUsername(connectionConfig);
        assertEquals("user@tenant2", username);

        connectionConfig.setDialectType(DialectType.MYSQL);
        username = OBConsoleDataSourceFactory.getUsername(connectionConfig);
        assertEquals("user", username);
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("password");

        String password = OBConsoleDataSourceFactory.getPassword(connectionConfig);
        assertEquals("password", password);
    }

    @Test
    public void testGetJdbcParams() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(63306);
        connectionConfig.setUsername("user");
        connectionConfig.setPassword("password");
        connectionConfig.setDefaultSchema("schema");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);

        Map<String, String> jdbcUrlParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        assertEquals(15, jdbcUrlParams.size());
        assertEquals("64000000", jdbcUrlParams.get("maxAllowedPacket"));
        assertNull(jdbcUrlParams.get("useSSL"));

        connectionConfig.setSslConfig(new SSLConfig(true, new SSLFileEntry("keyStoreFilePath", "keyStoreFilePassword")));
        jdbcUrlParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        assertEquals(18, jdbcUrlParams.size());
        assertEquals("true", jdbcUrlParams.get("useSSL"));
        assertEquals("keyStoreFilePath", jdbcUrlParams.get("trustStore"));
        assertEquals("keyStoreFilePassword", jdbcUrlParams.get("trustStorePassword"));
        assertEquals("keyStoreFilePath", jdbcUrlParams.get("keyStore"));
        assertEquals("keyStoreFilePassword", jdbcUrlParams.get("keyStorePassword"));
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(63306);
        connectionConfig.setUsername("user");
        connectionConfig.setPassword("password");
        connectionConfig.setDefaultSchema("schema");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
        assertEquals(DialectType.OB_ORACLE, factory.getDialectType());

        BasicDataSource bds = (BasicDataSource) dataSource;
        assertEquals("localhost", bds.getUrl().split("://")[1].split("/")[0]);
        assertEquals("user", bds.getUsername());
        assertEquals("password", bds.getPassword());
    }
}