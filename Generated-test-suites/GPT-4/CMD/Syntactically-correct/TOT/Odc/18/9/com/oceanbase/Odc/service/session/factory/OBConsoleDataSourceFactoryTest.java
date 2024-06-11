package com.oceanbase.odc.service.session.factory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

public class OBConsoleDataSourceFactoryTest {
    @Mock
    private ConnectionConfig connectionConfig;

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        when(connectionConfig.getHost()).thenReturn("127.0.0.1");
        when(connectionConfig.getPort()).thenReturn(3306);
        when(connectionConfig.getDefaultSchema()).thenReturn("public");
        String url = obConsoleDataSourceFactory.getJdbcUrl();
        Assert.assertNotEquals("", url);
        Assert.assertTrue(url.contains("127.0.0.1"));
        Assert.assertTrue(url.contains("3306"));
        Assert.assertTrue(url.contains("public"));
    }

    @Test
    public void testGetUsername() {
        when(connectionConfig.getUsername()).thenReturn("admin");
        when(connectionConfig.getTenantName()).thenReturn("tenant1");
        String username = OBConsoleDataSourceFactory.getUsername(connectionConfig);
        Assert.assertEquals("admin@tenant1", username);
    }

    @Test
    public void testGetPassword() {
        when(connectionConfig.getPassword()).thenReturn("password");
        String password = OBConsoleDataSourceFactory.getPassword(connectionConfig);
        Assert.assertEquals("password", password);
    }

    @Test
    public void testGetJdbcParams() {
        final Map<String, String> jdbcParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        Assert.assertEquals("64000000", jdbcParams.get("maxAllowedPacket"));
        Assert.assertEquals("5000", jdbcParams.get("connectTimeout"));
        // add other asserts
    }
    
    @Test
    public void testGetDataSource() {
        when(connectionConfig.getLoginType()).thenReturn(ConnectType.STATELESS);
        DataSource ds = obConsoleDataSourceFactory.getDataSource();
        Assert.assertNotNull(ds);
    }
    
    @Test
    public void testGetDialectType() {
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        DialectType type = obConsoleDataSourceFactory.getDialectType();
        Assert.assertEquals(DialectType.MYSQL, type);
    }

    // add more tests for other methods and for different scenarios.
}
