
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.oceanbase.odc.common.json.JacksonFactory;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.connection.CloudMetadataClient;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { BeanConfiguration.class })
public class BeanConfigurationTest {

    @Autowired
    private BeanConfiguration beanConfiguration;

    @MockBean
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertEquals(BCryptPasswordEncoder.class, passwordEncoder.getClass());
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);
        assertEquals(JacksonFactory.class, objectMapper.getClass());
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(httpFirewall);
        assertEquals(DefaultHttpFirewall.class, httpFirewall.getClass());
        assertEquals(true, ((DefaultHttpFirewall) httpFirewall).isAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertFalse(cloudMetadataClient.supportsCloudMetadata());
        assertFalse(cloudMetadataClient.supportsTenantInstance());
        assertFalse(cloudMetadataClient.needsOBTenantName());
        assertFalse(cloudMetadataClient.needsSysTenantUser());
        assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());
    }

    @Test
    public void testCookieSerializer() {
        DefaultCookieSerializer cookieSerializer = (DefaultCookieSerializer) beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }

}
