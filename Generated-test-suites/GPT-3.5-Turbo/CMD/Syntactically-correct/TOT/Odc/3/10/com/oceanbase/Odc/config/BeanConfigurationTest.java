import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfiguration;

    @Before
    public void setUp() {
        beanConfiguration = new BeanConfiguration();
    }

    @Test
    public void testPasswordEncoder() {
        assertNotNull(beanConfiguration.passwordEncoder());
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(beanConfiguration.objectMapper(null));
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertNotNull(beanConfiguration.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        assertNotNull(beanConfiguration.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        assertNotNull(beanConfiguration.cookieSerializer());
    }

    @Test
    public void testNullCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        nullCloudMetadataClient.init();
        assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
        assertFalse(nullCloudMetadataClient.supportsTenantInstance());
        assertFalse(nullCloudMetadataClient.needsOBTenantName());
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
    }

    @Test(expected = UnsupportedException.class)
    public void testListInstances() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        nullCloudMetadataClient.listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testListTenants() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        nullCloudMetadataClient.listTenants("instanceId");
    }

    @Test(expected = UnsupportedException.class)
    public void testGetTenant() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        nullCloudMetadataClient.getTenant("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testListDatabaseUsers() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
    }

    @Test
    public void testGetTenantEndpoint() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        assertNotNull(nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"));
    }

    @Test(expected = UnsupportedException.class)
    public void testGetSysTenantUser() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        nullCloudMetadataClient.getSysTenantUser("instanceId");
    }
}