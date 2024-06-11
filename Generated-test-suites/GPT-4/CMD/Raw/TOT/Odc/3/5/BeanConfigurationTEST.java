import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;

import static org.junit.Assert.*;

public class BeanConfigurationTest {

    private SensitivePropertyHandler sensitivePropertyHandler;
  
    // Test 
    @Test
    public void testPasswordEncoder() {
        BeanConfiguration configuration = new BeanConfiguration();
        PasswordEncoder passwordEncoder = configuration.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof PasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        BeanConfiguration configuration = new BeanConfiguration();
        ObjectMapper objectMapper = configuration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);
        assertTrue(objectMapper instanceof ObjectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        BeanConfiguration configuration = new BeanConfiguration();
        HttpFirewall httpFirewall = configuration.defaultHttpFirewall();
        assertNotNull(httpFirewall);
        assertTrue(httpFirewall instanceof HttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration configuration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = configuration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        BeanConfiguration configuration = new BeanConfiguration();
        CookieSerializer cookieSerializer = configuration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertTrue(cookieSerializer instanceof CookieSerializer);
    }

}