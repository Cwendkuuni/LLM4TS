import com.oceanbase.odc.config.BeanConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import static org.junit.Assert.*;

public class BeanConfigurationTest {
    private BeanConfiguration config;

    @Before
    public void setUp() throws Exception {
        config = new BeanConfiguration();
    }

    @Test
    public void passwordEncoder() {
        PasswordEncoder encoder = config.passwordEncoder();
        assertNotNull(encoder);
    }

    @Test
    public void objectMapper() {
        ObjectMapper objectMapper = config.objectMapper(new SensitivePropertyHandler());
        assertNotNull(objectMapper);
    }

    @Test
    public void defaultHttpFirewall() {
        HttpFirewall firewall = config.defaultHttpFirewall();
        assertNotNull(firewall);
    }

    @Test
    public void cloudMetadataClient() {
        CloudMetadataClient client = config.cloudMetadataClient();
        assertNotNull(client);
    }

    @Test
    public void cookieSerializer() {
        CookieSerializer serializer = config.cookieSerializer();
        assertNotNull(serializer);
    }
}