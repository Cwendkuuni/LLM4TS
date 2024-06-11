import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfig;

    @Before
    public void initialize() {
        beanConfig = new BeanConfiguration();
    }

    @Test
    public void testPasswordEncoder() {
        assertTrue(beanConfig.passwordEncoder() instanceof BCryptPasswordEncoder);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectMapperNullSensitivePropertyHandler() {
        beanConfig.objectMapper(null);
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertTrue(beanConfig.defaultHttpFirewall() instanceof DefaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        assertTrue(beanConfig.cloudMetadataClient() instanceof BeanConfiguration.NullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        assertTrue(beanConfig.cookieSerializer() instanceof DefaultCookieSerializer);
    }

    @Test(expected = UnsupportedException.class)
    public void testListInstancesUnsupported() {
        beanConfig.cloudMetadataClient().listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testGetTenantUnsupported() {
        beanConfig.cloudMetadataClient().getTenant("invalidInstanceId", "invalidTenantId");
    }
}