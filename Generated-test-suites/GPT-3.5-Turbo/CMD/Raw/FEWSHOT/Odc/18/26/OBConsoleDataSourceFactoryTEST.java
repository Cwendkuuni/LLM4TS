package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testConstructorWithAutoCommit() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertNotNull(factory);
    }

    @Test
    public void testConstructorWithInitConnection() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        assertNotNull(factory);
    }

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String jdbcUrl = factory.getJdbcUrl();
        assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        SingleConnectionDataSource dataSource = (SingleConnectionDataSource) factory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertNotNull(factory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        OBConsoleDataSourceFactory copy = (OBConsoleDataSourceFactory) factory.deepCopy();
        assertNotNull(copy);
        assertEquals(factory.getJdbcUrl(), copy.getJdbcUrl());
    }

    @Test
    public void testResetUsername() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetUsername(value -> "newUsername");
        assertEquals("newUsername", factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetPassword(value -> "newPassword");
        assertEquals("newPassword", factory.getPassword());
    }

    @Test
    public void testResetHost() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetHost(value -> "newHost");
        assertEquals("newHost", factory.getHost());
    }

    @Test
    public void testResetPort() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetPort(value -> 3306);
        assertEquals(Integer.valueOf(3306), factory.getPort());
    }

    @Test
    public void testResetSchema() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetSchema(value -> "newSchema");
        assertEquals("newSchema", factory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetParameters(value -> {
            value.put("newKey", "newValue");
            return value;
        });
        assertEquals("newValue", factory.getJdbcParams().get("newKey"));
    }

}