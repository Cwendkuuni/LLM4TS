
package com.oceanbase.odc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;

import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;


public class BeanConfigurationTest {

    private BeanConfiguration configuration = new BeanConfiguration();

    @Test
    public void testPasswordEncoderBean() {
        assertTrue(configuration.passwordEncoder() instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        //Testing this properly requires mocking SensitivePropertyHandler and might not be straightforward
        assertNotNull(configuration.objectMapper(null));
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertTrue(configuration.defaultHttpFirewall() instanceof DefaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        assertTrue(configuration.cloudMetadataClient() instanceof CloudMetadataClient);
        assertFalse(configuration.cloudMetadataClient().supportsCloudMetadata());
        assertFalse(configuration.cloudMetadataClient().supportsTenantInstance());
        assertTrue(configuration.cloudMetadataClient().includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, configuration.cloudMetadataClient().oceanbaseAccessMode());
        assertNotNull(configuration.cloudMetadataClient().getTenantEndpoint("test", "test"));
    }

    @Test
    public void testCookieSerializer() {
        assertTrue(configuration.cookieSerializer() instanceof DefaultCookieSerializer);
    }

    @Test(expected = UnsupportedException.class)
    public void testCloudMetadataClientExceptions() {
        configuration.cloudMetadataClient().listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testCloudMetadataClientGetTenant() {
        configuration.cloudMetadataClient().getTenant("test", "test");
    }

    @Test(expected = UnsupportedException.class)
    public void testCloudMetadataClientListDatabaseUsers() {
        configuration.cloudMetadataClient().listDatabaseUsers("test", "test");
    }

    @Test(expected = UnsupportedException.class)
    public void testCloudMetadataClientGetSysTenantUser() {
        configuration.cloudMetadataClient().getSysTenantUser("test");
    }
}
