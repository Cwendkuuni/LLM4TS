import com.oceanbase.odc.config.BeanConfiguration;
import org.junit.Test;
import org.junit.Assert;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        Assert.assertNotNull(beanConfiguration.passwordEncoder());
    }

    @Test
    public void testObjectMapper() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        Assert.assertNotNull(beanConfiguration.objectMapper(new SensitivePropertyHandler()));
    }

    @Test
    public void testDefaultHttpFirewall() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        Assert.assertNotNull(beanConfiguration.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        Assert.assertNotNull(beanConfiguration.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        Assert.assertNotNull(beanConfiguration.cookieSerializer());
    }

    @Test
    public void testNullCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        Assert.assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
        Assert.assertFalse(nullCloudMetadataClient.supportsTenantInstance());
        Assert.assertFalse(nullCloudMetadataClient.needsOBTenantName());
        Assert.assertFalse(nullCloudMetadataClient.needsSysTenantUser());
        Assert.assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        Assert.assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
    }
}