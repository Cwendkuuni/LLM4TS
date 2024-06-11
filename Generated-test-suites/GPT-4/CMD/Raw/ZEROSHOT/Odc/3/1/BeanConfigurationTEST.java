
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfiguration;
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Before
    public void setUp() {
        sensitivePropertyHandler = Mockito.mock(SensitivePropertyHandler.class);
        beanConfiguration = new BeanConfiguration();
    }

    @Test
    public void passwordEncoderTest() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();

        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void objectMapperTest() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);

        assertNotNull(objectMapper);
    }

    @Test
    public void defaultHttpFirewallTest() {
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();

        assertNotNull(httpFirewall);
        assertTrue(httpFirewall instanceof DefaultHttpFirewall);
    }
    
    @Test
    public void cloudMetadataClientTest () {
        CloudMetadataClient client = beanConfiguration.cloudMetadataClient();
       
        assertNotNull(client);
        assertEquals(false, client.supportsCloudMetadata());
        assertEquals(false, client.supportsTenantInstance());
        assertEquals(false, client.needsOBTenantName());
        assertEquals(false, client.needsSysTenantUser());
        assertEquals(true, client.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, client.oceanbaseAccessMode());
    }
    
    @Test(expected = UnsupportedException.class)
    public void listInstancesTest() {
        CloudMetadataClient client = beanConfiguration.cloudMetadataClient();
        
        client.listInstances();
    }

    @Test
    public void cookieSerializerTest() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();

        assertNotNull(cookieSerializer);
        assertTrue(cookieSerializer instanceof DefaultCookieSerializer);
    }
}
