
import static org.junit.Assert.*;
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
    public void testNullCloudMetadataClient_init() {
        BeanConfiguration.NullCloudMetadataClient nullClient = new BeanConfiguration.NullCloudMetadataClient();
        nullClient.init();
    }

    // Additional test cases for NullCloudMetadataClient methods with appropriate edge cases and exceptions
    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClient_listInstances() {
        BeanConfiguration.NullCloudMetadataClient nullClient = new BeanConfiguration.NullCloudMetadataClient();
        nullClient.listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClient_listTenants() {
        BeanConfiguration.NullCloudMetadataClient nullClient = new BeanConfiguration.NullCloudMetadataClient();
        nullClient.listTenants("instanceId");
    }

    // Add more test methods for other methods in NullCloudMetadataClient with edge cases and exceptions

}
