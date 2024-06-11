import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertNotNull(beanConfig.passwordEncoder());
    }

    @Test
    public void testObjectMapper() {
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertNotNull(beanConfig.objectMapper(new SensitivePropertyHandler()));
    }

    @Test
    public void testDefaultHttpFirewall() {
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertNotNull(beanConfig.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertNotNull(beanConfig.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertNotNull(beanConfig.cookieSerializer());
    }

    @Test
    public void testNullCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient nullClient = new BeanConfiguration.NullCloudMetadataClient();
        assertNotNull(nullClient);
        assertTrue(nullClient.supportsCloudMetadata() == false);
        assertTrue(nullClient.supportsTenantInstance() == false);
        assertTrue(nullClient.needsOBTenantName() == false);
        assertTrue(nullClient.needsSysTenantUser() == false);
        assertTrue(nullClient.includeClusterNameForJdbcUsername() == true);
        assertEquals(OceanBaseAccessMode.DIRECT, nullClient.oceanbaseAccessMode());
    }
}