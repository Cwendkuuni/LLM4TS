import static org.junit.Assert.*;
import org.junit.Test;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        // Test PasswordEncoder creation
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertNotNull(beanConfig.passwordEncoder());
    }

    @Test
    public void testObjectMapper() {
        // Test ObjectMapper creation with SensitivePropertyHandler
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertNotNull(beanConfig.objectMapper(new SensitivePropertyHandler()));
    }

    @Test
    public void testDefaultHttpFirewall() {
        // Test DefaultHttpFirewall creation
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertNotNull(beanConfig.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        // Test CloudMetadataClient creation
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertNotNull(beanConfig.cloudMetadataClient());
        assertEquals(new NullCloudMetadataClient().supportsCloudMetadata(), false);
    }

    @Test
    public void testCookieSerializer() {
        // Test CookieSerializer creation
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertNotNull(beanConfig.cookieSerializer());
        assertEquals(beanConfig.cookieSerializer().getCookieName(), "JSESSIONID");
    }

    @Test
    public void testNullCloudMetadataClient() {
        // Test NullCloudMetadataClient methods
        BeanConfiguration.NullCloudMetadataClient nullClient = new BeanConfiguration.NullCloudMetadataClient();
        assertEquals(nullClient.supportsCloudMetadata(), false);
        assertEquals(nullClient.supportsTenantInstance(), false);
        assertEquals(nullClient.needsOBTenantName(), false);
        assertEquals(nullClient.needsSysTenantUser(), false);
        assertEquals(nullClient.includeClusterNameForJdbcUsername(), true);
        assertEquals(nullClient.oceanbaseAccessMode(), OceanBaseAccessMode.DIRECT);
    }
}