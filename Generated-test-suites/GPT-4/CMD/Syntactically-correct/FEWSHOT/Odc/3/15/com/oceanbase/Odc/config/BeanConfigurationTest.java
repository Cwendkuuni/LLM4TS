
package com.oceanbase.odc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.web.http.DefaultCookieSerializer;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {
    
    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;
    
    private final BeanConfiguration config = new BeanConfiguration();

    @Test
    public void testPasswordEncoder() {
        assertTrue(config.passwordEncoder() instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(config.objectMapper(sensitivePropertyHandler));
    }
    
    @Test
    public void testDefaultHttpFirewall() {
        assertTrue(config.defaultHttpFirewall().isAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        assertFalse(config.cloudMetadataClient().supportsCloudMetadata());
        assertFalse(config.cloudMetadataClient().supportsTenantInstance());
        assertFalse(config.cloudMetadataClient().needsOBTenantName());
        assertFalse(config.cloudMetadataClient().needsSysTenantUser());
        assertTrue(config.cloudMetadataClient().includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, config.cloudMetadataClient().oceanbaseAccessMode());
    }
    
    @Test
    public void testCookieSerializer() {
        DefaultCookieSerializer serializer = (DefaultCookieSerializer) config.cookieSerializer();
        assertEquals("JSESSIONID", serializer.getCookieName());
    }

    // Test Exceptions
    @Test(expected=UnsupportedException.class)
    public void testListInstances() {
        List<OBInstance> instances = config.cloudMetadataClient().listInstances();
    }

    @Test(expected=UnsupportedException.class)
    public void testListTenants() {
        List<OBTenant> tenants = config.cloudMetadataClient().listTenants("instanceId");
    }

    @Test(expected=UnsupportedException.class)
    public void testGetTenant() {
        OBTenant tenant = config.cloudMetadataClient().getTenant("instanceId", "tenantId");
    }

    @Test(expected=UnsupportedException.class)
    public void testListDatabaseUsers() {
        List<OBDatabaseUser> databaseUsers = config.cloudMetadataClient().listDatabaseUsers("instanceId", "tenantId");
    }

    @Test(expected=UnsupportedException.class)
    public void testGetSysTenantUser() {
        OBDatabaseUser sysTenantUser = config.cloudMetadataClient().getSysTenantUser("instanceId");
    }
}
