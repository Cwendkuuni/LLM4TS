package com.oceanbase.odc.config;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


import static org.junit.Assert.*;

public class BeanConfigurationTest {
	
    private BeanConfiguration beanConfiguration;
    private SensitivePropertyHandler sensitivePropertyHandler;
    private NullCloudMetadataClient nullCloudMetadataClient;

    @Before
    public void setUp() {
        beanConfiguration = new BeanConfiguration();
        sensitivePropertyHandler = Mockito.mock(SensitivePropertyHandler.class);
        nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
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
        assertNotNull(beanConfiguration.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        assertNotNull(beanConfiguration.cookieSerializer());
        assertEquals("JSESSIONID", ((DefaultCookieSerializer) beanConfiguration.cookieSerializer()).getCookieName());
    }

    @Test(expected = UnsupportedException.class)
    public void testListInstances() {
        nullCloudMetadataClient.listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testListTenants() {
        nullCloudMetadataClient.listTenants("instanceId");
    }

    @Test(expected = UnsupportedException.class)
    public void testGetTenant() {
        nullCloudMetadataClient.getTenant("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testListDatabaseUsers() {
        nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
    }

    @Test
    public void testGetTenantEndpoint() {
        OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
        assertNotNull(endpoint);
        Assert.assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
    }

    @Test(expected = UnsupportedException.class)
    public void testGetSysTenantUser() {
        nullCloudMetadataClient.getSysTenantUser("instanceId");
    }
}