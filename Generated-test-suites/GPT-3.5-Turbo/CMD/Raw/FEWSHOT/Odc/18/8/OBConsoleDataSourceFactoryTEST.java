package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.MYSQL);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String jdbcUrl = factory.getJdbcUrl();

        assertEquals("jdbc:mysql://localhost:3306/test_schema", jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("user");
        connectionConfig.setDialectType(DialectType.MYSQL);

        String username = OBConsoleDataSourceFactory.getUsername(connectionConfig);

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
        connectionConfig.setDialectType(DialectType.MYSQL);

        Map<String, String> jdbcParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);

        assertNotNull(jdbcParams);
        assertEquals("64000000", jdbcParams.get("maxAllowedPacket"));
        assertEquals("true", jdbcParams.get("allowMultiQueries"));
        assertEquals("5000", jdbcParams.get("connectTimeout"));
        assertEquals(OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR, jdbcParams.get("zeroDateTimeBehavior"));
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.MYSQL);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = factory.getDataSource();

        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DialectType dialectType = factory.getDialectType();

        assertEquals(DialectType.MYSQL, dialectType);
    }

    @Test
    public void testDeepCopy() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.MYSQL);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        CloneableDataSourceFactory copyFactory = factory.deepCopy();

        assertNotNull(copyFactory);
        assertEquals(factory.getJdbcUrl(), ((OBConsoleDataSourceFactory) copyFactory).getJdbcUrl());
    }

    @Test
    public void testResetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("user");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetUsername(value -> "newuser");

        assertEquals("newuser", factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("password");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetPassword(value -> "newpassword");

        assertEquals("newpassword", factory.getPassword());
    }

    @Test
    public void testResetHost() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetHost(value -> "newhost");

        assertEquals("newhost", factory.getHost());
    }

    @Test
    public void testResetPort() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPort(3306);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetPort(value -> 8080);

        assertEquals(Integer.valueOf(8080), factory.getPort());
    }

    @Test
    public void testResetSchema() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDefaultSchema("test_schema");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetSchema(value -> "new_schema");

        assertEquals("new_schema", factory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        Map<String, String> params = new HashMap<>();
        params.put("key1", "value1");
        params.put("key2", "value2");
        factory.resetParameters(value -> params);

        assertEquals(params, factory.getParameters());
    }

    @Test
    public void testGetConnectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setType(ConnectType.MASTER);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        ConnectType connectType = factory.getConnectType();

        assertEquals(ConnectType.MASTER, connectType);
    }

    @Test
    public void testGetDefaultSchema() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.MYSQL);

        String defaultSchema = OBConsoleDataSourceFactory.getDefaultSchema(connectionConfig);

        assertEquals("test_schema", defaultSchema);
    }

}