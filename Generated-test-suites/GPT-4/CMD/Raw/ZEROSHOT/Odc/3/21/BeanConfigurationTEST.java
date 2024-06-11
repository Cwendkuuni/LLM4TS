
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import static org.junit.Assert.*;

public class BeanConfigurationTest {

    BeanConfiguration beanConfig;
    SensitivePropertyHandler sensitivePropertyHandler;

    @Before
    public void setUp() {
        beanConfig = new BeanConfiguration();
        sensitivePropertyHandler = new SensitivePropertyHandler() {
            @Override
            public String decrypt(String ciphertext) {
                return null;
            }

            @Override
            public String encrypt(String plaintext) {
                return null;
            }
        };
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfig.passwordEncoder();
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(beanConfig.objectMapper(sensitivePropertyHandler));
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall httpFirewall = beanConfig.defaultHttpFirewall();
        assertNotNull(httpFirewall);
        assertTrue(httpFirewall instanceof HttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfig.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertFalse(cloudMetadataClient.supportsCloudMetadata());
        assertFalse(cloudMetadataClient.supportsTenantInstance());
        assertFalse(cloudMetadataClient.needsOBTenantName());
        assertFalse(cloudMetadataClient.needsSysTenantUser());
        assertTrue(cloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());

        OBTenantEndpoint endpoint = cloudMetadataClient.getTenantEndpoint("instance", "tenant");
        assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer serializer = beanConfig.cookieSerializer();
        assertTrue(serializer instanceof DefaultCookieSerializer);
    }
}
