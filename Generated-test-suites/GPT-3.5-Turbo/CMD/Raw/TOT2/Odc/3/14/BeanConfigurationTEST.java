import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.DefaultCookieSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        assertNotNull(encoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper mapper = new BeanConfiguration().objectMapper(sensitivePropertyHandler);
        assertNotNull(mapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        DefaultHttpFirewall firewall = new BeanConfiguration().defaultHttpFirewall();
        assertNotNull(firewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient client = new BeanConfiguration().cloudMetadataClient();
        assertNotNull(client);
    }

    @Test
    public void testCookieSerializer() {
        DefaultCookieSerializer serializer = new BeanConfiguration().cookieSerializer();
        assertNotNull(serializer);
        assertEquals("JSESSIONID", serializer.getCookieName());
    }

    @Test
    public void testNullCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient nullClient = new BeanConfiguration.NullCloudMetadataClient();
        assertFalse(nullClient.supportsCloudMetadata());
        assertFalse(nullClient.supportsTenantInstance());
        assertFalse(nullClient.needsOBTenantName());
        assertFalse(nullClient.needsSysTenantUser());
        assertTrue(nullClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullClient.oceanbaseAccessMode());
        assertThrows(UnsupportedException.class, nullClient::listInstances);
        assertThrows(UnsupportedException.class, () -> nullClient.listTenants("instanceId"));
        assertThrows(UnsupportedException.class, () -> nullClient.getTenant("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> nullClient.listDatabaseUsers("instanceId", "tenantId"));
        OBTenantEndpoint endpoint = nullClient.getTenantEndpoint("instanceId", "tenantId");
        assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
        assertThrows(UnsupportedException.class, () -> nullClient.getSysTenantUser("instanceId"));
    }

}