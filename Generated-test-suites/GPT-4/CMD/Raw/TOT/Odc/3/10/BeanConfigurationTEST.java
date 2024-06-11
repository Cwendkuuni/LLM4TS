import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;

public class BeanConfigurationTest {

    private BeanConfiguration config;

    @Before
    public void setUp() throws Exception {
        config = new BeanConfiguration();
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = config.passwordEncoder();
        assertNotNull(encoder);
    }

    @Test
    public void testObjectMapper() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            config.objectMapper(null);
        });
        assertNotNull(exception);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall firewall = config.defaultHttpFirewall();
        assertNotNull(firewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient client = config.cloudMetadataClient();
        assertNotNull(client);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer serializer = config.cookieSerializer();
        assertNotNull(serializer);
    }

    @Test
    public void testUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> {
            config.cloudMetadataClient().listInstances();
        });
        assertThrows(UnsupportedOperationException.class, () -> {
            config.cloudMetadataClient().getTenant("", "");
        });
    }
}