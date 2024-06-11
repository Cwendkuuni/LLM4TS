package com.oceanbase.odc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.security.web.firewall.DefaultHttpFirewall;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        assertNotNull(new BeanConfiguration().passwordEncoder());
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(new BeanConfiguration().objectMapper(new SensitivePropertyHandler()));
    }

    @Test
    public void testDefaultHttpFirewall() {
        DefaultHttpFirewall defaultHttpFirewall = new BeanConfiguration().defaultHttpFirewall();
        assertTrue(defaultHttpFirewall instanceof DefaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        assertNotNull(new BeanConfiguration().cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        assertNotNull(new BeanConfiguration().cookieSerializer());
    }

    @Test
    public void testNullCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        assertEquals(false, nullCloudMetadataClient.supportsCloudMetadata());
        assertEquals(false, nullCloudMetadataClient.supportsTenantInstance());
        assertEquals(false, nullCloudMetadataClient.needsOBTenantName());
        assertEquals(false, nullCloudMetadataClient.needsSysTenantUser());
        assertEquals(true, nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertNotNull(nullCloudMetadataClient.oceanbaseAccessMode());
        // Add more assertions based on NullCloudMetadataClient methods
    }
}