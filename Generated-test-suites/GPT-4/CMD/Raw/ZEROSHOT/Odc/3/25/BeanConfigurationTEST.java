import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.junit.runner.RunWith;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    private BeanConfiguration beanConfiguration;

    @Before
    public void setUp(){
        beanConfiguration = new BeanConfiguration();
    }

    @Test
    public void testPasswordEncoder(){
        assertTrue(beanConfiguration.passwordEncoder() instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper(){
        assertNotNull(beanConfiguration.objectMapper(sensitivePropertyHandler));
    }

    @Test
    public void testDefaultHttpFirewall(){
        DefaultHttpFirewall firewall = (DefaultHttpFirewall) beanConfiguration.defaultHttpFirewall();
        assertTrue(firewall.getAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient(){
        BeanConfiguration.NullCloudMetadataClient client = (BeanConfiguration.NullCloudMetadataClient)beanConfiguration.cloudMetadataClient();
        assertFalse(client.supportsCloudMetadata());
        assertFalse(client.supportsTenantInstance());
        assertFalse(client.needsOBTenantName());
        assertFalse(client.needsSysTenantUser());
        assertTrue(client.includeClusterNameForJdbcUsername());
    }

    @Test
    public void testCookieSerializer(){
        DefaultCookieSerializer serializer = (DefaultCookieSerializer) beanConfiguration.cookieSerializer();
        assertEquals("JSESSIONID", serializer.getCookieName());
    }
}