
package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {
    private ConnectionConfig connectionConfig;

    @Before
    public void init() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("testHost");
        connectionConfig.setPort(8080);
        connectionConfig.setUsername("testUsername");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setType(ConnectType.JDBC);

        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("testFile");
        sslFileEntry.setKeyStoreFilePassword("testPass");

        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);

        connectionConfig.setSslConfig(sslConfig);
        connectionConfig.setSslFileEntry(sslFileEntry);

        OBTenantEndpoint obTenantEndpoint = new OBTenantEndpoint();
        obTenantEndpoint.setProxyHost("testProxyHost");
        obTenantEndpoint.setProxyPort(8081);

        connectionConfig.setEndpoint(obTenantEndpoint);

        Map<String, String> jdbcParameters = new HashMap<>();
        jdbcParameters.put("testKey", "testValue");
        connectionConfig.setJdbcUrlParameters(jdbcParameters);
    }

    @Test
    public void testGetUsername() {
        String username = OBConsoleDataSourceFactory.getUsername(connectionConfig);
        assertNotNull(username);
        assertTrue(username.contains("@"));
        assertTrue(username.contains("#"));
    }

    @Test
    public void testGetPassword() {
        String password = OBConsoleDataSourceFactory.getPassword(connectionConfig);
        assertEquals("testPassword", password);
    }

    @Test
    public void testGetJdbcUrl() {
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetDataSource() {
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertNotNull(obConsoleDataSourceFactory.getDataSource());
    }

    @Test
    public void testGetDialectType() {
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DialectType dialectType = obConsoleDataSourceFactory.getDialectType();
        assertEquals(DialectType.MYSQL, dialectType);
    }

    @Test
    public void testGetDefaultSchema() {
        String defaultSchema = OBConsoleDataSourceFactory.getDefaultSchema(connectionConfig);
        assertEquals("testSchema", defaultSchema);
    }
}
