
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertTrue(beanConfig.passwordEncoder() instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertEquals(beanConfig.objectMapper(mock(SensitivePropertyHandler.class)).getClass(), ObjectMapper.class);
    }

    @Test
    public void testDefaultHttpFirewall() {
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertTrue(beanConfig.defaultHttpFirewall() instanceof DefaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertTrue(beanConfig.cloudMetadataClient() instanceof BeanConfiguration.NullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertTrue(beanConfig.cookieSerializer() instanceof DefaultCookieSerializer);
    }

    @Test
    public void testNullCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient nullClient = new BeanConfiguration.NullCloudMetadataClient();
        assertTrue(nullClient.needsOBTenantName());
        assertTrue(nullClient.needsSysTenantUser());
        assertTrue(nullClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullClient.oceanbaseAccessMode());
    }

    // Additional test cases for specific methods in NullCloudMetadataClient class
    @Test(expected = UnsupportedException.class)
    public void testListInstances() {
        BeanConfiguration.NullCloudMetadataClient nullClient = new BeanConfiguration.NullCloudMetadataClient();
        nullClient.listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testListTenants() {
        BeanConfiguration.NullCloudMetadataClient nullClient = new BeanConfiguration.NullCloudMetadataClient();
        nullClient.listTenants("instanceId");
    }

    @Test(expected = UnsupportedException.class)
    public void testGetTenant() {
        BeanConfiguration.NullCloudMetadataClient nullClient = new BeanConfiguration.NullCloudMetadataClient();
        nullClient.getTenant("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testListDatabaseUsers() {
        BeanConfiguration.NullCloudMetadataClient nullClient = new BeanConfiguration.NullCloudMetadataClient();
        nullClient.listDatabaseUsers("instanceId", "tenantId");
    }

    @Test
    public void testGetTenantEndpoint() {
        BeanConfiguration.NullCloudMetadataClient nullClient = new BeanConfiguration.NullCloudMetadataClient();
        assertNotNull(nullClient.getTenantEndpoint("instanceId", "tenantId"));
    }

    @Test(expected = UnsupportedException.class)
    public void testGetSysTenantUser() {
        BeanConfiguration.NullCloudMetadataClient nullClient = new BeanConfiguration.NullCloudMetadataClient();
        nullClient.getSysTenantUser("instanceId");
    }
}
