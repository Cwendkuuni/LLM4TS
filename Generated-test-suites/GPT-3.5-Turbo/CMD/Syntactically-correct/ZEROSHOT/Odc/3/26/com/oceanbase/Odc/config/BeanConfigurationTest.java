import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.oceanbase.odc.config.*;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @Mock
    SensitivePropertyHandler sensitivePropertyHandler;

    BeanConfiguration beanConfig;

    @Before
    public void setUp() {
        beanConfig = new BeanConfiguration();
    }

    @Test
    public void testPasswordEncoder() {
        assertNotNull(beanConfig.passwordEncoder());
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(beanConfig.objectMapper(sensitivePropertyHandler));
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertNotNull(beanConfig.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        assertNotNull(beanConfig.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        assertNotNull(beanConfig.cookieSerializer());
    }

    @Test
    public void testNullCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = beanConfig.new NullCloudMetadataClient();
        assertNotNull(nullCloudMetadataClient);
        assertEquals(false, nullCloudMetadataClient.supportsCloudMetadata());
        assertEquals(false, nullCloudMetadataClient.supportsTenantInstance());
        assertEquals(false, nullCloudMetadataClient.needsOBTenantName());
        assertEquals(false, nullCloudMetadataClient.needsSysTenantUser());
        assertEquals(true, nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
        try {
            nullCloudMetadataClient.listInstances();
            fail("Expected UnsupportedException");
        } catch (UnsupportedException e) {
            // Expected
        }
        try {
            nullCloudMetadataClient.listTenants("123");
            fail("Expected UnsupportedException");
        } catch (UnsupportedException e) {
            // Expected
        }
        try {
            nullCloudMetadataClient.getTenant("123", "456");
            fail("Expected UnsupportedException");
        } catch (UnsupportedException e) {
            // Expected
        }
        try {
            nullCloudMetadataClient.listDatabaseUsers("123", "456");
            fail("Expected UnsupportedException");
        } catch (UnsupportedException e) {
            // Expected
        }
        OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint("123", "456");
        assertNotNull(endpoint);
        assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
        try {
            nullCloudMetadataClient.getSysTenantUser("123");
            fail("Expected UnsupportedException");
        } catch (UnsupportedException e) {
            // Expected
        }
    }

}