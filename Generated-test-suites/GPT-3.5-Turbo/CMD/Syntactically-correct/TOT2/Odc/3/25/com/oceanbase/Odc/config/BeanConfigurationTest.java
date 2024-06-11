import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import javax.validation.constraints.NotBlank;

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
        assertNotNull(beanConfiguration.objectMapper(mock(SensitivePropertyHandler.class)));
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
    public void testNullCloudMetadataClientMethods() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        assertThrows(UnsupportedException.class, nullCloudMetadataClient::listInstances);
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listTenants("instanceId"));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenant("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
        assertNotNull(nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser("instanceId"));
    }

}