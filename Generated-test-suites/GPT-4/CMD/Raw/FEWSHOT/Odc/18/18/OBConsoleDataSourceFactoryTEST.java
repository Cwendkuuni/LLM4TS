
package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;


public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory factory;
    private ConnectionConfig config;

    @Before
    public void setUp(){
        config = Mockito.mock(ConnectionConfig.class);
        factory = new OBConsoleDataSourceFactory(config, true);
    }

    @Test
    public void testGetDatasource() {
        DataSource actual = factory.getDataSource();
        assertTrue(actual instanceof SingleConnectionDataSource);
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory clone = factory.deepCopy();
        assertEquals(clone.getConnectionConfig(), factory.getConnectionConfig());
    }

    @Test
    public void testGetUsername() {
        Mockito.when(config.getUsername()).thenReturn("testUser");
        assertEquals("testUser", OBConsoleDataSourceFactory.getUsername(config));
    }

    @Test
    public void testGetPassword() {
        Mockito.when(config.getPassword()).thenReturn("testPassword");
        assertEquals("testPassword", OBConsoleDataSourceFactory.getPassword(config));
    }

    @Test
    public void testGetJdbcUrl() {
        assertNull(factory.getJdbcUrl());
    }

    @Test
    public void testGetDbUser() {
        Mockito.when(config.getUsername()).thenReturn("user1");
        Mockito.when(config.getDialectType()).thenReturn(DialectType.MYSQL);
        String expected = "user1";
        String actual = OBConsoleDataSourceFactory.getDbUser(config);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDefaultSchema() {
        Mockito.when(config.getDefaultSchema()).thenReturn("schema1");
        Mockito.when(config.getDialectType()).thenReturn(DialectType.MYSQL);
        String expected = "schema1";
        String actual = OBConsoleDataSourceFactory.getDefaultSchema(config);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetJdbcParams() {
        OBTenantEndpoint endpoint = new OBTenantEndpoint();
        SSLConfig sslConfig = new SSLConfig();
        SSLFileEntry sslFileEntry = new SSLFileEntry();

        Mockito.when(config.getEndpoint()).thenReturn(endpoint);
        Mockito.when(config.getSslConfig()).thenReturn(sslConfig);
        Mockito.when(config.getSslFileEntry()).thenReturn(sslFileEntry);
        Mockito.when(config.getJdbcUrlParameters()).thenReturn(new HashMap<>());

        Map<String, String> actual = OBConsoleDataSourceFactory.getJdbcParams(config);

        assertEquals(9, actual.keySet().size());
        assertTrue(actual.keySet().contains("maxAllowedPacket"));
        assertTrue(actual.keySet().contains("allowMultiQueries"));
    }
}
