
package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testOBConsoleDataSourceFactory() {
        ConnectionConfig config = Mockito.mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true);
        assertNotNull(factory);
    }

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig config = Mockito.mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true);
        String jdbcUrl = factory.getJdbcUrl();
        assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig config = Mockito.mock(ConnectionConfig.class);
        String username = OBConsoleDataSourceFactory.getUsername(config);
        assertNotNull(username);
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig config = Mockito.mock(ConnectionConfig.class);
        String password = OBConsoleDataSourceFactory.getPassword(config);
        assertNotNull(password);
    }

    @Test
    public void testGetJdbcParams() {
        ConnectionConfig config = Mockito.mock(ConnectionConfig.class);
        OBTenantEndpoint mockEndPoint = Mockito.mock(OBTenantEndpoint.class);
        SSLConfig mockSSLConfig = Mockito.mock(SSLConfig.class);
        Mockito.when(config.getEndpoint()).thenReturn(mockEndPoint);
        Mockito.when(config.getSslConfig()).thenReturn(mockSSLConfig);
        Mockito.when(mockSSLConfig.getEnabled()).thenReturn(true);
        Map<String, String> jdbcParams = OBConsoleDataSourceFactory.getJdbcParams(config);
        assertNotNull(jdbcParams);
    }

    @Test
    public void testResetUsername() {
        ConnectionConfig config = Mockito.mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true);
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        Mockito.when(mapper.map(Mockito.anyString())).thenReturn("new username");
        factory.resetUsername(mapper);
        assertEquals("new username", factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        ConnectionConfig config = Mockito.mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true);
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        Mockito.when(mapper.map(Mockito.anyString())).thenReturn("new password");
        factory.resetPassword(mapper);
        assertEquals("new password", factory.getPassword());
    }

    @Test
    public void testResetHost() {
        ConnectionConfig config = Mockito.mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true);
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        Mockito.when(mapper.map(Mockito.anyString())).thenReturn("new host");
        factory.resetHost(mapper);
        assertEquals("new host", factory.host);
    }
}
