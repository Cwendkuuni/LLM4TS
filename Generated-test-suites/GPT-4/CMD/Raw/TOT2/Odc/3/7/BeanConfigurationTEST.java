import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;

public class BeanConfigurationTest {
    BeanConfiguration configuration;
    SensitivePropertyHandler handler;

    @Before
    public void setup() {
        configuration = new BeanConfiguration();
        handler = new SensitivePropertyHandler();
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = configuration.passwordEncoder();
        assertNotNull(encoder);
        String password = "password";
        assertTrue(encoder.matches(password, encoder.encode(password)));
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(configuration.objectMapper(handler));
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall firewall = configuration.defaultHttpFirewall();
        assertNotNull(firewall);
        assertTrue(firewall.getFirewallConfig().isAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        assertNotNull(configuration.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        assertNotNull(configuration.cookieSerializer());
        assertEquals("JSESSIONID", configuration.cookieSerializer().getCookieName());
    }

    // TODO: Add test cases for NullCloudMetadataClient class and its methods

}