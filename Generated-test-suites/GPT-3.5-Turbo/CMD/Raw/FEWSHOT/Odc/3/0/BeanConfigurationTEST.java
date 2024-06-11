package com.oceanbase.odc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class BeanConfigurationTest {
    
    @Test
    public void testPasswordEncoder() {
        BeanConfiguration config = new BeanConfiguration();
        assertNotNull(config.passwordEncoder());
    }
    
    @Test
    public void testObjectMapper() {
        BeanConfiguration config = new BeanConfiguration();
        assertNotNull(config.objectMapper(null));
    }
    
    @Test
    public void testDefaultHttpFirewall() {
        BeanConfiguration config = new BeanConfiguration();
        assertNotNull(config.defaultHttpFirewall());
    }
    
    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration config = new BeanConfiguration();
        assertNotNull(config.cloudMetadataClient());
    }
    
    @Test
    public void testCookieSerializer() {
        BeanConfiguration config = new BeanConfiguration();
        assertNotNull(config.cookieSerializer());
        assertEquals("JSESSIONID", config.cookieSerializer().getCookieName());
    }
    
    @Test
    public void testNullCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient client = new BeanConfiguration.NullCloudMetadataClient();
        assertTrue(client.supportsCloudMetadata());
        assertTrue(client.supportsTenantInstance());
        assertFalse(client.needsOBTenantName());
        assertFalse(client.needsSysTenantUser());
        assertTrue(client.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, client.oceanbaseAccessMode());
    }
}