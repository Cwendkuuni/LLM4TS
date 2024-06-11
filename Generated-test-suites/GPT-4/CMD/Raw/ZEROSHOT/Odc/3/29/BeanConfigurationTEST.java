
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.List;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.CookieSerializer;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    BeanConfiguration beanConfiguration = new BeanConfiguration();

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler sensitivePropertyHandler = null;
        assertNotNull(beanConfiguration.objectMapper(sensitivePropertyHandler));
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(httpFirewall);
        assertTrue(httpFirewall instanceof DefaultHttpFirewall);
        assertTrue(((DefaultHttpFirewall) httpFirewall).isAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        assertNotNull(beanConfiguration.cloudMetadataClient());
        assertTrue(beanConfiguration.cloudMetadataClient() instanceof NullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertTrue(cookieSerializer instanceof DefaultCookieSerializer);
        assertEquals("JSESSIONID", ((DefaultCookieSerializer) cookieSerializer).getCookieName());
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient client = new NullCloudMetadataClient();

        assertFalse(client.supportsCloudMetadata());
        assertFalse(client.supportsTenantInstance());
        assertFalse(client.needsOBTenantName());
        assertFalse(client.needsSysTenantUser());
        assertTrue(client.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, client.oceanbaseAccessMode());

        assertThrows(UnsupportedOperationException.class, client::listInstances);
        assertThrows(UnsupportedOperationException.class, () -> client.listTenants(null));
        assertThrows(UnsupportedOperationException.class, () -> client.getTenant(null, null));
        assertThrows(UnsupportedOperationException.class, () -> client.listDatabaseUsers(null, null));
        assertThrows(UnsupportedOperationException.class, () -> client.getSysTenantUser(null));

        OBTenantEndpoint tenantEndpoint = client.getTenantEndpoint(null, null);
        assertNotNull(tenantEndpoint);
        assertEquals(OceanBaseAccessMode.DIRECT, tenantEndpoint.getAccessMode());
    }
}
