package com.oceanbase.odc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        assertNotNull(beanConfiguration.passwordEncoder());
    }

    @Test
    public void testObjectMapper() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        assertNotNull(beanConfiguration.objectMapper());
    }

    @Test
    public void testDefaultHttpFirewall() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        assertNotNull(beanConfiguration.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        assertNotNull(beanConfiguration.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        assertNotNull(beanConfiguration.cookieSerializer());
        assertEquals("JSESSIONID", beanConfiguration.cookieSerializer().getCookieName());
    }

    @Test
    public void testNullCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        assertTrue(nullCloudMetadataClient.supportsCloudMetadata());
        assertTrue(nullCloudMetadataClient.supportsTenantInstance());
        assertTrue(nullCloudMetadataClient.needsOBTenantName());
        assertTrue(nullCloudMetadataClient.needsSysTenantUser());
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
        assertNotNull(nullCloudMetadataClient.listInstances()); 
        assertNotNull(nullCloudMetadataClient.listTenants("instanceId"));
        assertNotNull(nullCloudMetadataClient.getTenant("instanceId", "tenantId"));
        assertNotNull(nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
        assertNotNull(nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"));
        assertNotNull(nullCloudMetadataClient.getSysTenantUser("instanceId"));
    }
}