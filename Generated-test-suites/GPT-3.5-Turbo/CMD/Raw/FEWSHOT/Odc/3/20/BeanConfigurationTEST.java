//Test cases for BeanConfiguration class

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        assertNotNull(beanConfiguration.passwordEncoder());
        assertTrue(beanConfiguration.passwordEncoder() instanceof BCryptPasswordEncoder);
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
        assertTrue(beanConfiguration.cloudMetadataClient() instanceof BeanConfiguration.NullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        assertNotNull(beanConfiguration.cookieSerializer());
    }

    @Test
    public void testNullCloudMetadataClientMethods() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        
        assertNotNull(nullCloudMetadataClient);
        assertTrue(nullCloudMetadataClient.supportsCloudMetadata());
        assertTrue(nullCloudMetadataClient.supportsTenantInstance());
        assertTrue(nullCloudMetadataClient.needsOBTenantName());
        assertTrue(nullCloudMetadataClient.needsSysTenantUser());
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
    }

}