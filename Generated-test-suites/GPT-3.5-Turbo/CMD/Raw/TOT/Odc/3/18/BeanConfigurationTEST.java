import static org.junit.Assert.*;
import org.junit.Test;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        assertNotNull(passwordEncoder.encode("testpassword"));
    }

    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler sensitivePropertyHandler = new SensitivePropertyHandler();
        ObjectMapper objectMapper = new BeanConfiguration().objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = new BeanConfiguration().defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        assertFalse(cloudMetadataClient.supportsCloudMetadata());
        assertFalse(cloudMetadataClient.supportsTenantInstance());
        assertFalse(cloudMetadataClient.needsOBTenantName());
        assertFalse(cloudMetadataClient.needsSysTenantUser());
        assertTrue(cloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());
    }

    @Test(expected = UnsupportedException.class)
    public void testListInstancesException() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        cloudMetadataClient.listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testListTenantsException() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        cloudMetadataClient.listTenants("instanceId");
    }

    @Test(expected = UnsupportedException.class)
    public void testGetTenantException() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        cloudMetadataClient.getTenant("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testListDatabaseUsersException() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
    }

    @Test
    public void testGetTenantEndpoint() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        OBTenantEndpoint endpoint = cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
        assertNotNull(endpoint);
        assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
    }

    @Test(expected = UnsupportedException.class)
    public void testGetSysTenantUserException() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        cloudMetadataClient.getSysTenantUser("instanceId");
    }

}