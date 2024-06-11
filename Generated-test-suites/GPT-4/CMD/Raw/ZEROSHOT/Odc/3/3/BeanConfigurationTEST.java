import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.DefaultCookieSerializer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BeanConfigurationTest {
    private BeanConfiguration beanConfig;
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Before
    public void setUp() {
        beanConfig = new BeanConfiguration();
        sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
    }

    @Test
    public void testPasswordEncoder() {
        assertEquals(BCryptPasswordEncoder.class, beanConfig.passwordEncoder().getClass());
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(beanConfig.objectMapper(sensitivePropertyHandler));
    }

    @Test
    public void testDefaultHttpFirewall() {
        DefaultHttpFirewall firewall = (DefaultHttpFirewall) beanConfig.defaultHttpFirewall();
        assertTrue(firewall.isAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        assertEquals(BeanConfiguration.NullCloudMetadataClient.class, beanConfig.cloudMetadataClient().getClass());
    }

    @Test
    public void testCookieSerializer() {
        DefaultCookieSerializer serializer = (DefaultCookieSerializer) beanConfig.cookieSerializer();
        assertEquals("JSESSIONID", serializer.getCookieName());
    }
    
    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient client = beanConfig.new NullCloudMetadataClient();
        assertFalse(client.supportsCloudMetadata());
        assertFalse(client.supportsTenantInstance());
        assertFalse(client.needsOBTenantName());
        assertFalse(client.needsSysTenantUser());
        assertTrue(client.includeClusterNameForJdbcUsername());
    }

    // Add more tests for the other methods in NullCloudMetadataClient class
}