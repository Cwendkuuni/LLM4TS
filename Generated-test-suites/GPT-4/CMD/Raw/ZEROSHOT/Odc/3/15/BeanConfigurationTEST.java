
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.DefaultCookieSerializer;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

public class BeanConfigurationTest {

    BeanConfiguration testClass = new BeanConfiguration();

    SensitivePropertyHandler sensitivePropertyHandler = new SensitivePropertyHandler();

    NullCloudMetadataClient nullCloudMetadataClient = testClass.cloudMetadataClient();

    @Test
    public void passwordEncoderTest() {
        assertNotNull(testClass.passwordEncoder());
        assertTrue(testClass.passwordEncoder() instanceof BCryptPasswordEncoder);
    }

    @Test
    public void objectMapperTest() {
        assertNotNull(testClass.objectMapper(sensitivePropertyHandler));
    }

    @Test
    public void defaultHttpFirewallTest() {
        assertNotNull(testClass.defaultHttpFirewall());
        assertTrue(testClass.defaultHttpFirewall() instanceof DefaultHttpFirewall);
    }

    @Test
    public void cloudMetadataClientTest() {
        assertNotNull(testClass.cloudMetadataClient());
        assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
        assertFalse(nullCloudMetadataClient.supportsTenantInstance());
        assertFalse(nullCloudMetadataClient.needsOBTenantName());
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());

        try {
            nullCloudMetadataClient.listInstances();
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }

        try {
            nullCloudMetadataClient.listTenants("Test");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }

        try {
            nullCloudMetadataClient.getTenant("InstanceID", "TenantID");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }

        try {
            nullCloudMetadataClient.listDatabaseUsers("InstanceID", "TenantID");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }

        OBTenantEndpoint result = nullCloudMetadataClient.getTenantEndpoint("InstanceID", "TenantID");
        assertNotNull(result);
        assertEquals(OceanBaseAccessMode.DIRECT, result.getAccessMode());

        try {
            nullCloudMetadataClient.getSysTenantUser("InstanceID");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void cookieSerializerTest() {
        assertNotNull(testClass.cookieSerializer());
        assertTrue(testClass.cookieSerializer() instanceof DefaultCookieSerializer);
        assertEquals("JSESSIONID", ((DefaultCookieSerializer) testClass.cookieSerializer()).getCookieName());
    }
}
