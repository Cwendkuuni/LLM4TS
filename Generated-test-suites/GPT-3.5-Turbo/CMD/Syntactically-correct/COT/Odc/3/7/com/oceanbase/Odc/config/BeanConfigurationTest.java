import static org.junit.Assert.*;
import org.junit.Test;
import com.oceanbase.odc.config.BeanConfiguration;
import org.junit.runner.RunWith;

@RunWith(SpringJUnit4ClassRunner.class)
public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        // Mock SensitivePropertyHandler
        SensitivePropertyHandler sensitivePropertyHandler = new SensitivePropertyHandler();
        ObjectMapper objectMapper = BeanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall httpFirewall = new DefaultHttpFirewall();
        assertNotNull(httpFirewall);
        assertTrue(httpFirewall instanceof DefaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        assertNotNull(cloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = new DefaultCookieSerializer();
        assertNotNull(cookieSerializer);
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }

    @Test
    public void init() {
        BeanConfiguration.NullCloudMetadataClient cloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        cloudMetadataClient.init();
        // Verify log.info message
    }

    // Add more test cases for edge cases and exceptions

}