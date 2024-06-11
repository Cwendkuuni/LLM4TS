import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
        // Mock SensitivePropertyHandler
        SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        assertNotNull(beanConfiguration.objectMapper(sensitivePropertyHandler));
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
        BeanConfiguration.NullCloudMetadataClient nullClient = beanConfiguration.new NullCloudMetadataClient();
        assertTrue(nullClient.supportsCloudMetadata());
        assertTrue(nullClient.supportsTenantInstance());
        assertFalse(nullClient.needsOBTenantName());
        assertFalse(nullClient.needsSysTenantUser());
        assertTrue(nullClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullClient.oceanbaseAccessMode());
        assertThrows(UnsupportedException.class, nullClient::listInstances);
        assertThrows(UnsupportedException.class, () -> nullClient.listTenants("instanceId"));
        assertThrows(UnsupportedException.class, () -> nullClient.getTenant("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> nullClient.listDatabaseUsers("instanceId", "tenantId"));
        assertNotNull(nullClient.getTenantEndpoint("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> nullClient.getSysTenantUser("instanceId"));
    }
}