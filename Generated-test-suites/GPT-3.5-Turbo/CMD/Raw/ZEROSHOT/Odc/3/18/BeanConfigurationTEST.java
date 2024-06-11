import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.DefaultCookieSerializer;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoderBean() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapperBean() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        ObjectMapper objectMapper = beanConfiguration.objectMapper(new SensitivePropertyHandler());
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewallBean() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(httpFirewall);
        assertTrue(httpFirewall instanceof DefaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClientBean() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
    }

    @Test
    public void testCookieSerializerBean() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertTrue(cookieSerializer instanceof DefaultCookieSerializer);
    }

}