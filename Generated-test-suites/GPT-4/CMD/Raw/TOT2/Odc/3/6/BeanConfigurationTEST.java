import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.web.firewall.HttpFirewall;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import org.springframework.session.web.http.CookieSerializere;

public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;
    
    private BeanConfiguration beanConfiguration;

    @Before
    public void setup() { 
        MockitoAnnotations.initMocks(this);
        beanConfiguration = new BeanConfiguration();
    }
    
    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = beanConfiguration.passwordEncoder();
        assertNotNull(encoder);
    }
    
    @Test
    public void testObjectMapper() {
        ObjectMapper mapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(mapper);
    }
    
    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall firewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(firewall);
    }
    
    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient client = beanConfiguration.cloudMetadataClient();
        assertNotNull(client);
        assertFalse(client.supportsCloudMetadata());
        assertFalse(client.supportsTenantInstance());
        assertFalse(client.needsOBTenantName());
        assertFalse(client.needsSysTenantUser());
        assertTrue(client.includeClusterNameForJdbcUsername());
        assertEquals(client.oceanbaseAccessMode(), OceanBaseAccessMode.DIRECT);
        assertThrows(UnsupportedException.class, client::listInstances);
        assertThrows(UnsupportedException.class, () -> client.listTenants("test_instance"));
        assertThrows(UnsupportedException.class, () -> client.getTenant("test_instance", "test_tenant"));
        assertThrows(UnsupportedException.class, () -> client.listDatabaseUsers("test_instance", "test_tenant"));
        assertNotNull(client.getTenantEndpoint("test_instance", "test_tenant"));
        assertThrows(UnsupportedException.class, () -> client.getSysTenantUser("test_instance"));
    }
    
    @Test
    public void testCookieSerializer() {
        CookieSerializer serializer = beanConfiguration.cookieSerializer();
        assertNotNull(serializer);
        assertEquals(serializer.getCookieName(), "JSESSIONID");
    }
}