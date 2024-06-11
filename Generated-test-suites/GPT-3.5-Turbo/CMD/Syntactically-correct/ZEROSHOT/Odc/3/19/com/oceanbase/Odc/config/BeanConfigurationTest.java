import org.junit.Test;
import static org.junit.Assert.*;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        assertNotNull(beanConfiguration.passwordEncoder());
    }

    @Test
    public void testObjectMapper() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        assertNotNull(beanConfiguration.objectMapper(new SensitivePropertyHandler()));
    }

    @Test
    public void testDefaultHttpFirewall() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        assertNotNull(beanConfiguration.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        assertNotNull(beanConfiguration.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        assertNotNull(beanConfiguration.cookieSerializer());
    }

    @Test
    public void testNullCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
        assertFalse(nullCloudMetadataClient.supportsTenantInstance());
        assertFalse(nullCloudMetadataClient.needsOBTenantName());
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
        assertThrows(UnsupportedException.class, nullCloudMetadataClient::listInstances);
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listTenants("instanceId"));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenant("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
        assertNotNull(nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser("instanceId"));
    }
}