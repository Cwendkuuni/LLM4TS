import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.util.List;

public class BeanConfigurationTest {

    @Mock
    SensitivePropertyHandler sensitivePropertyHandler;

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration config = new BeanConfiguration();
        assertNotNull(config.passwordEncoder());
    }

    @Test
    public void testObjectMapper() {
        BeanConfiguration config = new BeanConfiguration();
        assertNotNull(config.objectMapper(sensitivePropertyHandler));
    }

    @Test
    public void testDefaultHttpFirewall() {
        BeanConfiguration config = new BeanConfiguration();
        assertNotNull(config.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient cloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        assertTrue(!cloudMetadataClient.supportsCloudMetadata());
        assertTrue(!cloudMetadataClient.supportsTenantInstance());
        assertTrue(!cloudMetadataClient.needsOBTenantName());
        assertTrue(!cloudMetadataClient.needsSysTenantUser());
        assertTrue(cloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());
    }

    @Test(expected = UnsupportedException.class)
    public void testListInstances() {
        BeanConfiguration.NullCloudMetadataClient cloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        cloudMetadataClient.listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testListTenants() {
        BeanConfiguration.NullCloudMetadataClient cloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        cloudMetadataClient.listTenants("instanceId");
    }

    @Test(expected = UnsupportedException.class)
    public void testGetTenant() {
        BeanConfiguration.NullCloudMetadataClient cloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        cloudMetadataClient.getTenant("instanceId","tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testListDatabaseUsers() {
        BeanConfiguration.NullCloudMetadataClient cloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        cloudMetadataClient.listDatabaseUsers("instanceId","tenantId");
    }

    @Test
    public void testGetTenantEndpoint() {
        BeanConfiguration.NullCloudMetadataClient cloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        assertNotNull(cloudMetadataClient.getTenantEndpoint("instanceId","tenantId"));
    }

    @Test(expected = UnsupportedException.class)
    public void testGetSysTenantUser() {
        BeanConfiguration.NullCloudMetadataClient cloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        cloudMetadataClient.getSysTenantUser("instanceId");
    }

}