import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import java.util.List;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfiguration;

    @Before
    public void setup() {
        beanConfiguration = new BeanConfiguration();
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(new SensitivePropertyHandler());
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(httpFirewall);
    }

    @Test
    public void testNullCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertFalse(cloudMetadataClient.supportsCloudMetadata());
        assertFalse(cloudMetadataClient.supportsTenantInstance());
        assertFalse(cloudMetadataClient.needsOBTenantName());
        assertFalse(cloudMetadataClient.needsSysTenantUser());
        assertTrue(cloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());

        try {
            cloudMetadataClient.listInstances();
            fail("Expected UnsupportedException");
        } catch (UnsupportedException e) {
            // Expected
        }
        try {
            cloudMetadataClient.listTenants("instanceId");
            fail("Expected UnsupportedException");
        } catch (UnsupportedException e) {
            // Expected
        }
        try {
            cloudMetadataClient.getTenant("instanceId", "tenantId");
            fail("Expected UnsupportedException");
        } catch (UnsupportedException e) {
            // Expected
        }
        try {
            cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
            fail("Expected UnsupportedException");
        } catch (UnsupportedException e) {
            // Expected
        }
        try {
            cloudMetadataClient.getSysTenantUser("instanceId");
            fail("Expected UnsupportedException");
        } catch (UnsupportedException e) {
            // Expected
        }

        OBTenantEndpoint endpoint = cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
        assertNotNull(endpoint);
        assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertEquals("JSESSIONID", ((DefaultCookieSerializer) cookieSerializer).getCookieName());
    }
}