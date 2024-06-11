import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPasswordEncoder() {
        assertNotNull(beanConfiguration.passwordEncoder());
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(beanConfiguration.objectMapper(sensitivePropertyHandler));
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertNotNull(beanConfiguration.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertFalse(cloudMetadataClient.supportsCloudMetadata());
        assertFalse(cloudMetadataClient.supportsTenantInstance());
        assertFalse(cloudMetadataClient.needsOBTenantName());
        assertFalse(cloudMetadataClient.needsSysTenantUser());
        assertTrue(cloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());
    }

    @Test(expected = UnsupportedException.class)
    public void testListInstances() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        cloudMetadataClient.listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testListTenants() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        cloudMetadataClient.listTenants("instanceId");
    }

    @Test(expected = UnsupportedException.class)
    public void testGetTenant() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        cloudMetadataClient.getTenant("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testListDatabaseUsers() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
    }

    @Test
    public void testGetTenantEndpoint() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        OBTenantEndpoint endpoint = cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
        assertNotNull(endpoint);
        assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
    }

    @Test(expected = UnsupportedException.class)
    public void testGetSysTenantUser() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        cloudMetadataClient.getSysTenantUser("instanceId");
    }
}