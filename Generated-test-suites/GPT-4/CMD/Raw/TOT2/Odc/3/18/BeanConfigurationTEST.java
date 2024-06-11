
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

// other necessary imports

public class BeanConfigurationTest {
    BeanConfiguration beanConfiguration;
    SensitivePropertyHandler sensitivePropertyHandler;

    @Before
    public void setup() {
        beanConfiguration = new BeanConfiguration();
        sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
    }

    @Test
    public void testPasswordEncoder() throws Exception {
        PasswordEncoder encoder = beanConfiguration.passwordEncoder();
        Assert.assertNotNull(encoder);
    }
    
    @Test
    public void testObjectMapper() throws Exception {
        ObjectMapper mapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        Assert.assertNotNull(mapper);
    }

    @Test
    public void testDefaultHttpFirewall() throws Exception {
        HttpFirewall firewall = beanConfiguration.defaultHttpFirewall();
        Assert.assertNotNull(firewall);
        Assert.assertEquals(true, firewall.getAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() throws Exception {
        CloudMetadataClient client = beanConfiguration.cloudMetadataClient();
        Assert.assertNotNull(client);
        Assert.assertEquals(false, client.supportsCloudMetadata());
        // Continue testing for all the methods in the client
    }

    @Test
    public void testCookieSerializer() throws Exception {
        CookieSerializer serializer = beanConfiguration.cookieSerializer();
        Assert.assertNotNull(serializer);
        Assert.assertEquals("JSESSIONID", serializer.getCookieName());
    }
}
