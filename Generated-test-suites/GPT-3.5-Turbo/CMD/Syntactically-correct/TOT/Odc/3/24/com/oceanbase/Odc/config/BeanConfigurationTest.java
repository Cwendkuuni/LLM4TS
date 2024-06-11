import static org.junit.Assert.*;

import org.junit.Test;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        // Test typical scenario
        PasswordEncoder encoder = new BeanConfiguration().passwordEncoder();
        assertNotNull(encoder);
        
        // Test edge case - different instances
        PasswordEncoder encoder2 = new BeanConfiguration().passwordEncoder();
        assertNotSame(encoder, encoder2);
    }

    @Test
    public void testObjectMapper() {
        // Test typical scenario
        ObjectMapper mapper = new BeanConfiguration().objectMapper(new SensitivePropertyHandler());
        assertNotNull(mapper);
        
        // Test edge case - sensitivePropertyHandler is null
        ObjectMapper mapper2 = new BeanConfiguration().objectMapper(null);
        assertNotNull(mapper2);
    }

    @Test
    public void testDefaultHttpFirewall() {
        // Test typical scenario
        HttpFirewall firewall = new BeanConfiguration().defaultHttpFirewall();
        assertNotNull(firewall);
        
        // Test edge case - allowing url encoded slash
        assertTrue(((DefaultHttpFirewall)firewall).isAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient client = new BeanConfiguration().cloudMetadataClient();
        assertNotNull(client);
        
        // Test error scenario - cloud metadata not supported
        assertFalse(client.supportsCloudMetadata());
    }

    @Test
    public void testCookieSerializer() {
        // Test typical scenario
        CookieSerializer serializer = new BeanConfiguration().cookieSerializer();
        assertNotNull(serializer);
        
        // Test edge case - setting cookie name
        assertEquals("JSESSIONID", serializer.getCookieName());
    }
}