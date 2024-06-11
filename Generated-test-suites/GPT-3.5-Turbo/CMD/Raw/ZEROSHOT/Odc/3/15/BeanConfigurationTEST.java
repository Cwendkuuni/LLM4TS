import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertNotNull(beanConfig.passwordEncoder());
    }

    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertNotNull(beanConfig.objectMapper(sensitivePropertyHandler));
    }

    @Test
    public void testDefaultHttpFirewall() {
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertNotNull(beanConfig.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertNotNull(beanConfig.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertNotNull(beanConfig.cookieSerializer());
    }

    @Test
    public void testNullCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient nullClient = new BeanConfiguration.NullCloudMetadataClient();
        assertNotNull(nullClient);
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
        assertNotNull(nullClient.getTenantEndpoint("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> nullClient.getSysTenantUser("instanceId"));
    }
}