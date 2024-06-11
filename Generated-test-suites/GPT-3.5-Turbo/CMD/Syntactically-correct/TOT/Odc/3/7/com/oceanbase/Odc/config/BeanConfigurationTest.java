import static org.junit.Assert.*;

import org.junit.Test;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler handler = new SensitivePropertyHandler();
        ObjectMapper mapper = BeanConfiguration.objectMapper(handler);
        assertNotNull(mapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall firewall = BeanConfiguration.defaultHttpFirewall();
        assertNotNull(firewall);
        assertTrue(firewall instanceof DefaultHttpFirewall);
        assertTrue(((DefaultHttpFirewall) firewall).isAllowUrlEncodedSlash());
    }

}