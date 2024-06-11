package com.oceanbase.odc.config;

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
        assertNotNull(beanConfiguration.objectMapper(null));
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
    }

    @Test
    public void testNullCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        assertTrue(nullCloudMetadataClient.supportsCloudMetadata());
        assertTrue(nullCloudMetadataClient.supportsTenantInstance());
        assertTrue(nullCloudMetadataClient.needsOBTenantName());
        assertTrue(nullCloudMetadataClient.needsSysTenantUser());
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertNotNull(nullCloudMetadataClient.oceanbaseAccessMode());
        assertNotNull(nullCloudMetadataClient.listInstances());
        assertNotNull(nullCloudMetadataClient.listTenants(""));
        assertNotNull(nullCloudMetadataClient.getTenant("", ""));
        assertNotNull(nullCloudMetadataClient.listDatabaseUsers("", ""));
        assertNotNull(nullCloudMetadataClient.getTenantEndpoint("", ""));
        assertNotNull(nullCloudMetadataClient.getSysTenantUser(""));
    }
}