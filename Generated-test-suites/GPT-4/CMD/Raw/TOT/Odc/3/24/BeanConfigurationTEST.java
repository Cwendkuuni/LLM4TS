import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import org.springframework.session.web.http.CookieSerializer;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfiguration;

    @Before
    public void setUp() {
        beanConfiguration = new BeanConfiguration();
    }

    @Test
    public void passwordEncoderTest() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void objectMapperTest() {
        SensitivePropertyHandler handler = Mockito.mock(SensitivePropertyHandler.class);
        ObjectMapper objectMapper = beanConfiguration.objectMapper(handler);
        assertNotNull(objectMapper);
    }

    @Test
    public void defaultHttpFirewallTest() {
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        assertTrue(httpFirewall.allowsUrlEncodedSlash());
    }

    @Test
    public void cloudMetadataClientTest() {
        CloudMetadataClient cloudMetadataClient  = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
    }

    @Test
    public void cookieSerializerTest() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }
}