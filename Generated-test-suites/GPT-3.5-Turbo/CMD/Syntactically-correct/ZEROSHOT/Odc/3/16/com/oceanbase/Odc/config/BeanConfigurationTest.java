import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        assertNotNull(beanConfiguration.passwordEncoder());
    }

    @Test
    public void testObjectMapper() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        assertNotNull(beanConfiguration.objectMapper(sensitivePropertyHandler));
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
        assertTrue(nullCloudMetadataClient.supportsCloudMetadata());
        assertTrue(nullCloudMetadataClient.supportsTenantInstance());
        assertTrue(nullCloudMetadataClient.needsOBTenantName());
        assertTrue(nullCloudMetadataClient.needsSysTenantUser());
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertNotNull(nullCloudMetadataClient.oceanbaseAccessMode());
        assertNotNull(nullCloudMetadataClient.listInstances());
        assertNotNull(nullCloudMetadataClient.listTenants("fake_instance_id"));
        assertNotNull(nullCloudMetadataClient.getTenant("fake_instance_id", "fake_tenant_id"));
        assertNotNull(nullCloudMetadataClient.listDatabaseUsers("fake_instance_id", "fake_tenant_id"));
        assertNotNull(nullCloudMetadataClient.getTenantEndpoint("fake_instance_id", "fake_tenant_id"));
        assertNotNull(nullCloudMetadataClient.getSysTenantUser("fake_instance_id"));
    }

}