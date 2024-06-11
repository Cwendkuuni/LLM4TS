package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testOBConsoleDataSourceFactory_defaultConstructor() {
        assertNull(new OBConsoleDataSourceFactory(null, null));
    }

    @Test
    public void testOBConsoleDataSourceFactory_withConnectionConfig() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1521);
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertEquals("localhost", dataSourceFactory.getHost());
        assertEquals(1521, dataSourceFactory.getPort());
        assertEquals("test_user", dataSourceFactory.getUsername());
        assertEquals("test_password", dataSourceFactory.getPassword());
        assertEquals(DialectType.OB_ORACLE, dataSourceFactory.getDialectType());
    }

    @Test
    public void testOBConsoleDataSourceFactory_getJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1521);
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        ConnectionExtensionPoint connectionExtensionPoint = ConnectionPluginUtil.getConnectionExtension(DialectType.OB_ORACLE);
        String expectedJdbcUrl = connectionExtensionPoint.generateJdbcUrl("localhost", 1521, null, null);
        assertEquals(expectedJdbcUrl, jdbcUrl);
    }

    @Test
    public void testOBConsoleDataSourceFactory_getDataSource() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1521);
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertTrue(dataSource instanceof SingleConnectionDataSource);
        SingleConnectionDataSource singleConnectionDataSource = (SingleConnectionDataSource) dataSource;
        assertNotNull(singleConnectionDataSource.getEventPublisher());
        assertEquals(dataSourceFactory.getJdbcUrl(), singleConnectionDataSource.getUrl());
        assertEquals(dataSourceFactory.getUsername(), singleConnectionDataSource.getUsername());
        assertEquals(dataSourceFactory.getPassword(), singleConnectionDataSource.getPassword());
        assertEquals(dataSourceFactory.getDriverClassName(), singleConnectionDataSource.getDriverClassName());
        assertTrue(singleConnectionDataSource.getConnectionProperties() instanceof Properties);
    }

    @Test
    public void testOBConsoleDataSourceFactory_getDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1521);
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertEquals(DialectType.OB_ORACLE, dataSourceFactory.getDialectType());
    }

    @Test
    public void testOBConsoleDataSourceFactory_deepCopy() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1521);
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        OBConsoleDataSourceFactory originalDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        OBConsoleDataSourceFactory clonedDataSourceFactory = (OBConsoleDataSourceFactory) originalDataSourceFactory.deepCopy();
        assertNotSame(originalDataSourceFactory, clonedDataSourceFactory);
        assertEquals(originalDataSourceFactory.getUsername(), clonedDataSourceFactory.getUsername());
        assertEquals(originalDataSourceFactory.getPassword(), clonedDataSourceFactory.getPassword());
        assertEquals(originalDataSourceFactory.getHost(), clonedDataSourceFactory.getHost());
        assertEquals(originalDataSourceFactory.getPort(), clonedDataSourceFactory.getPort());
        assertEquals(originalDataSourceFactory.getDialectType(), clonedDataSourceFactory.getDialectType());
    }

    @Test
    public void testOBConsoleDataSourceFactory_resetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1521);
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String oldUsername = dataSourceFactory.getUsername();
        CloneableDataSourceFactory.ValueMapper<String> mapper = (value) -> value + "_modified";
        dataSourceFactory.resetUsername(mapper);
        assertEquals(oldUsername + "_modified", dataSourceFactory.getUsername());
    }

    @Test
    public void testOBConsoleDataSourceFactory_resetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1521);
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String oldPassword = dataSourceFactory.getPassword();
        CloneableDataSourceFactory.ValueMapper<String> mapper = (value) -> value + "_modified";
        dataSourceFactory.resetPassword(mapper);
        assertEquals(oldPassword + "_modified", dataSourceFactory.getPassword());
    }

    @Test
    public void testOBConsoleDataSourceFactory_resetHost() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1521);
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String oldHost = dataSourceFactory.getHost();
        CloneableDataSourceFactory.ValueMapper<String> mapper = (value) -> value + "_modified";
        dataSourceFactory.resetHost(mapper);
        assertEquals(oldHost + "_modified", dataSourceFactory.getHost());
    }

    @Test
    public void testOBConsoleDataSourceFactory_resetPort() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1521);
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        int oldPort = dataSourceFactory.getPort();
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = (value) -> value + 1;
        dataSourceFactory.resetPort(mapper);
        assertEquals(oldPort + 1, dataSourceFactory.getPort());
    }

    @Test
    public void testOBConsoleDataSourceFactory_resetSchema() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1521);
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setDefaultSchema("test_schema");
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String oldSchema = dataSourceFactory.getDefaultSchema();
        CloneableDataSourceFactory.ValueMapper<String> mapper = (value) -> value + "_modified";
        dataSourceFactory.resetSchema(mapper);
        assertEquals(oldSchema + "_modified", dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testOBConsoleDataSourceFactory_resetParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1521);
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("test_param1", "test_value1");
        parameters.put("test_param2", "test_value2");
        connectionConfig.setJdbcUrlParameters(parameters);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        Map<String, String> oldParameters = dataSourceFactory.getParameters();
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = (value) -> {
            Map<String, String> newMap = new HashMap<>();
            newMap.putAll(value);
            newMap.put("test_param1_modified", "test_value1_modified");
            return newMap;
        };
        dataSourceFactory.resetParameters(mapper);
        assertEquals(oldParameters, dataSourceFactory.getParameters());
    }
}