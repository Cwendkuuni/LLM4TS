import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
        SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        ObjectMapper objectMapper = new BeanConfiguration().objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        // Test DefaultHttpFirewall bean creation
        HttpFirewall httpFirewall = new BeanConfiguration().defaultHttpFirewall();
        assertNotNull(httpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        // Test CloudMetadataClient bean creation
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
        assertTrue(cloudMetadataClient instanceof BeanConfiguration.NullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        // Test CookieSerializer bean creation
        CookieSerializer cookieSerializer = new BeanConfiguration().cookieSerializer();
        assertNotNull(cookieSerializer);
    }

    @Test
    public void testNullCloudMetadataClient() {
        // Test NullCloudMetadataClient behavior
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        
        // Test if initData method is called
        nullCloudMetadataClient.init();
        
        // Test exceptions from unsupported methods
        assertThrows(UnsupportedException.class, nullCloudMetadataClient::listInstances);
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listTenants("instanceId"));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenant("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser("instanceId"));
    }
}