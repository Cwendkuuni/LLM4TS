
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;

public class BeanConfigurationTest {

    private NullCloudMetadataClient underTest;
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Before
    public void setUp() {
       underTest = new BeanConfiguration().new NullCloudMetadataClient();
       sensitivePropertyHandler = Mockito.mock(SensitivePropertyHandler.class); 
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BeanConfiguration().passwordEncoder();
        String encodedPassword = passwordEncoder.encode("password");
        assertTrue(passwordEncoder.matches("password", encodedPassword));
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(new BeanConfiguration().objectMapper(sensitivePropertyHandler));
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertNotNull(new BeanConfiguration().defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        assertNotNull(new BeanConfiguration().cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = new BeanConfiguration().cookieSerializer();
        assertEquals(cookieSerializer.getCookieName(), "JSESSIONID");
    }

    @Test(expected = UnsupportedException.class)
    public void testListInstances() {
        underTest.listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testListTenants() {
        underTest.listTenants("instanceId");
    }

    @Test(expected = UnsupportedException.class)
    public void testGetTenant() {
        underTest.getTenant("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testListDatabaseUsers() {
        underTest.listDatabaseUsers("instanceId", "tenantId");
    }

    @Test
    public void testGetTenantEndpoint() {
        OBTenantEndpoint endpoint = underTest.getTenantEndpoint("instanceId", "tenantId");
        assertEquals(endpoint.getAccessMode(), OceanBaseAccessMode.DIRECT);
    }

    @Test(expected = UnsupportedException.class)
    public void testGetSysTenantUser() {
        underTest.getSysTenantUser("instanceId");
    }
}


