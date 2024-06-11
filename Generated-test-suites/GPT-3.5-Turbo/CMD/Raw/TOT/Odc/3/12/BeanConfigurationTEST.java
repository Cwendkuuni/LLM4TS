
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Test
    public void testPasswordEncoder() {
        assertTrue(beanConfiguration.passwordEncoder() instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(beanConfiguration.objectMapper(sensitivePropertyHandler));
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertNotNull(beanConfiguration.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        assertNotNull(beanConfiguration.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        assertNotNull(beanConfiguration.cookieSerializer());
    }

    @Test
    public void testNullCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertTrue(nullCloudMetadataClient.needsOBTenantName());
        assertFalse(nullCloudMetadataClient.supportsTenantInstance());
        assertThrows(UnsupportedException.class, nullCloudMetadataClient::listInstances);
    }
}
