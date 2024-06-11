import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Before;

public class BeanConfigurationTest { 

    private BeanConfiguration beanConfiguration;
    
    @Before
    public void setUp() {
        beanConfiguration = new BeanConfiguration();
    }
    
    @Test
    public void testBeanPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler sensitivePropertyHandler = new SensitivePropertyHandler();
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(httpFirewall);
        assertTrue(httpFirewall instanceof DefaultHttpFirewall);
        assertTrue(((DefaultHttpFirewall) httpFirewall).isAllowUrlEncodedSlash());
    }

    @Test
    public void cloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertTrue(cloudMetadataClient instanceof BeanConfiguration.NullCloudMetadataClient);
    }

    @Test
    public void cookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertTrue(cookieSerializer instanceof DefaultCookieSerializer);
        assertEquals("JSESSIONID", ((DefaultCookieSerializer) cookieSerializer).getCookieName());
    }
 
}