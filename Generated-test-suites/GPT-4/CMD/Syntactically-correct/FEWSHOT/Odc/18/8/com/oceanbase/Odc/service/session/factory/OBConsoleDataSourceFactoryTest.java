package com.oceanbase.odc.service.session.factory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.DialectType;

import javax.sql.DataSource;
import java.util.Map;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class OBConsoleDataSourceFactoryTest {

    @Mock
    ConnectionConfig connectionConfig;
    @Mock
    SSLConfig sslConfig; 
    @Mock
    SSLFileEntry sslFileEntry;
    @Mock
    OBTenantEndpoint obTenantEndpoint;
    @Mock
    ConnectionExtensionPoint connectionExtensionPoint;
    @Mock
    EventPublisher eventPublisher;
    @InjectMocks
    OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before 
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(sslFileEntry.getKeyStoreFilePath()).thenReturn("keyStoreFilePath");
        when(sslFileEntry.getKeyStoreFilePassword()).thenReturn("keyStoreFilePassword");
        when(connectionConfig.getSslFileEntry()).thenReturn(sslFileEntry);
        when(connectionConfig.getSslConfig()).thenReturn(sslConfig);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);
        when(connectionConfig.getOBTenantName()).thenReturn("tenantName1");           
        when(connectionConfig.getEndpoint()).thenReturn(obTenantEndpoint);
        when(connectionConfig.getUsername()).thenReturn("username1");
        when(connectionConfig.getPassword()).thenReturn("password1");
        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(8080);
        when(connectionConfig.getDefaultSchema()).thenReturn("defaultSchema1");  
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("testKey", "testValue");
        when(connectionConfig.getJdbcUrlParameters()).thenReturn(params);
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        obConsoleDataSourceFactory.setEventPublisher(eventPublisher);
    }

    @Test
    public void testGetUsername() {
        assertEquals("username1@tenantName1", obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("password1", obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.OB_MYSQL, obConsoleDataSourceFactory.getDialectType());
    }

    @Test                                                                                              
    public void testResetUsername() {
        obConsoleDataSourceFactory.resetUsername(value -> value += "Changed");
        assertEquals("username1@tenantName1Changed", obConsoleDataSourceFactory.getUsername());
    }

    @Test                                                                                              
    public void testResetPassword() {
        obConsoleDataSourceFactory.resetPassword(value -> value += "Changed");
        assertEquals("password1Changed", obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        obConsoleDataSourceFactory.resetHost(value -> value += "Changed");
        assertEquals("localhostChanged", obConsoleDataSourceFactory.host);
    }
    
    @Test
    public void testResetPort() {
        obConsoleDataSourceFactory.resetPort(value -> value += 50);
        assertEquals(8080+50, (int) obConsoleDataSourceFactory.port);
    }

    @Test
    public void testResetSchema() {
        obConsoleDataSourceFactory.resetSchema(value -> value += "Changed");
        assertEquals("defaultSchema1Changed", obConsoleDataSourceFactory.defaultSchema);
    }

    @Test
    public void testGetDataSource() {
        DataSource ds = obConsoleDataSourceFactory.getDataSource();
        assertEquals(ds instanceof DataSource, true);
    }
}