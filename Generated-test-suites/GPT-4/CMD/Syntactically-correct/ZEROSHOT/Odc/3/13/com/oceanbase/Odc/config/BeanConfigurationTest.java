package com.oceanbase.odc.config;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class BeanConfigurationTest {

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPasswordEncoder() {
        assertNotNull(beanConfiguration.passwordEncoder());
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(beanConfiguration.objectMapper(sensitivePropertyHandler));
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertNotNull(beanConfiguration.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient result = beanConfiguration.cloudMetadataClient();
        assertNotNull(result);
        assertFalse(result.supportsCloudMetadata());
        assertFalse(result.supportsTenantInstance());
        assertFalse(result.needsOBTenantName());
        assertFalse(result.needsSysTenantUser());
        assertTrue(result.includeClusterNameForJdbcUsername());
    }

    @Test
    public void testCookieSerializer() {
        assertNotNull(beanConfiguration.cookieSerializer());
    }

    // Tests for the nested class NullCloudMetadataClient
    @Test(expected = UnsupportedException.class)
    public void test_nullCloudMetadataClient_listInstances() {
        NullCloudMetadataClient client = new NullCloudMetadataClient();
        client.listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void test_nullCloudMetadataClient_listTenants() {
        NullCloudMetadataClient client = new NullCloudMetadataClient();
        client.listTenants("instanceId");
    }

    @Test(expected = UnsupportedException.class)
    public void test_nullCloudMetadataClient_getTenant() {
        NullCloudMetadataClient client = new NullCloudMetadataClient();
        client.getTenant("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void test_nullCloudMetadataClient_listDatabaseUsers() {
        NullCloudMetadataClient client = new NullCloudMetadataClient();
        client.listDatabaseUsers("instanceId", "tenantId");
    }

    @Test
    public void test_nullCloudMetadataClient_getTenantEndpoint() {
        NullCloudMetadataClient client = new NullCloudMetadataClient();
        assertNotNull(client.getTenantEndpoint("instanceId", "tenantId"));
    }

    @Test(expected = UnsupportedException.class)
    public void test_nullCloudMetadataClient_getSysTenantUser() {
        NullCloudMetadataClient client = new NullCloudMetadataClient();
        client.getSysTenantUser("instanceId");
    }
}
