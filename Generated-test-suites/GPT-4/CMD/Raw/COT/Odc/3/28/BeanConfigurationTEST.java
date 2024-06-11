
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfiguration;
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Before
    public void setUp() {
        beanConfiguration = new BeanConfiguration();
        sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
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
    public void testHttpFirewall() {
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
    public void testNullCloudMetadataClientMethods() {
        BeanConfiguration.NullCloudMetadataClient ncmc = beanConfiguration.new NullCloudMetadataClient();

        assertEquals(false, ncmc.supportsCloudMetadata());
        assertEquals(false, ncmc.supportsTenantInstance());
        assertEquals(false, ncmc.needsOBTenantName());
        assertEquals(false, ncmc.needsSysTenantUser());
        assertEquals(true, ncmc.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, ncmc.oceanbaseAccessMode());

        assertThrows(UnsupportedException.class, () -> { ncmc.listInstances(); });
        assertThrows(UnsupportedException.class, () -> { ncmc.listTenants("some-instanceId"); });
        assertThrows(UnsupportedException.class, () -> { ncmc.getTenant("some-instanceId", "some-tenantId"); });
        assertThrows(UnsupportedException.class, () -> { ncmc.listDatabaseUsers("some-instanceId", "some-tenantId"); });
        assertThrows(UnsupportedException.class, () -> { ncmc.getSysTenantUser("some-instanceId"); });

        OBTenantEndpoint endpoint = ncmc.getTenantEndpoint("some-instanceId", "some-tenantId");
        assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
    }
}
