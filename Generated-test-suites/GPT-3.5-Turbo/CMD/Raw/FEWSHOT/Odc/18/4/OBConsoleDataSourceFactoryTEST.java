package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.sql.DataSource;

import org.junit.Test;

import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("schema");
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setUsername("username");
        String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
        assertEquals("jdbc:mysql://localhost:3306/schema", jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("user");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        assertEquals("\"user\"", OBConsoleDataSourceFactory.getUsername(connectionConfig));
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("password");
        assertEquals("password", OBConsoleDataSourceFactory.getPassword(connectionConfig));
    }

    @Test
    public void testGetJdbcParams() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.getJdbcUrlParameters().put("key", "value");
        Map<String, String> jdbcParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        assertNotNull(jdbcParams);
        assertEquals("value", jdbcParams.get("key"));
    }

    // Add more test methods for other functionality in OBConsoleDataSourceFactory class

}