
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;

@RunWith(SpringRunner.class)
@Import(BeanConfiguration.class)
public class BeanConfigurationTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpFirewall httpFirewall;

    @Autowired
    private CookieSerializer cookieSerializer;

    @MockBean
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Autowired
    private CloudMetadataClient cloudMetadataClient;

    @Test
    public void testPasswordEncoder() {
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        assertNull(objectMapper);
    }

    @Test
    public void testHttpFirewall() {
        assertNotNull(httpFirewall);
    }

   @Test
    public void testCookieSerializer() {
        assertNotNull(cookieSerializer);
    }

    @Test(expected = UnsupportedException.class)
    public void testCloudMetadataClient_exception() {
        cloudMetadataClient.listInstances();
    }

    @Test
    public void testCloudMetadataClient_accessMode() {
        assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());
    }
}
