import static org.junit.Assert.*;
import org.junit.Test;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        // Test PasswordEncoder bean creation
        PasswordEncoder passwordEncoder = new BeanConfiguration().passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        // Test ObjectMapper bean creation
        SensitivePropertyHandler sensitivePropertyHandler = new SensitivePropertyHandler();
        ObjectMapper objectMapper = new BeanConfiguration().objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        // Test HttpFirewall bean creation
        HttpFirewall httpFirewall = new BeanConfiguration().defaultHttpFirewall();
        assertNotNull(httpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        // Test CloudMetadataClient bean creation
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        // Test CookieSerializer bean creation
        CookieSerializer cookieSerializer = new BeanConfiguration().cookieSerializer();
        assertNotNull(cookieSerializer);
    }

    @Test
    public void testNullCloudMetadataClient() {
        // Test NullCloudMetadataClient methods
        NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        assertTrue(nullCloudMetadataClient.supportsCloudMetadata());
        assertTrue(nullCloudMetadataClient.supportsTenantInstance());
        assertFalse(nullCloudMetadataClient.needsOBTenantName());
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listInstances());
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listTenants("instanceId"));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenant("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
        OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
        assertNotNull(endpoint);
        assertTrue(endpoint.getAccessMode() == OceanBaseAccessMode.DIRECT);
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser("instanceId"));
    }

}