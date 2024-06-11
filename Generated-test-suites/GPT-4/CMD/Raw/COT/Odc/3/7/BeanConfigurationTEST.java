import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.web.firewall.HttpFirewall;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import org.springframework.session.web.http.CookieSerializer;

public class BeanConfigurationTest {
    private BeanConfiguration beanConfig;
    private SensitivePropertyHandler sensitivePropertyHandlerMock;
    
    @Before
    public void setUp() {
        beanConfig = new BeanConfiguration();
        sensitivePropertyHandlerMock = Mockito.mock(SensitivePropertyHandler.class);
    }
    
    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfig.passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = beanConfig.objectMapper(sensitivePropertyHandlerMock);
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall firewall = beanConfig.defaultHttpFirewall();
        assertNotNull(firewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient client = beanConfig.cloudMetadataClient();
        assertNotNull(client);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer serializer = beanConfig.cookieSerializer();
        assertNotNull(serializer);
    }

    @Test(expected = UnsupportedException.class)
    public void testListInstancesException() {
        beanConfig.cloudMetadataClient().listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testListTenantsException() {
        beanConfig.cloudMetadataClient().listTenants("instanceTest");
    }
}