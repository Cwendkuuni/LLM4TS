import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
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

    @Test(expected = IllegalArgumentException.class)
    public void testObjectMapper_nullSensitivePropertyHandler() {
        beanConfiguration.objectMapper(null);
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
        BeanConfiguration.NullCloudMetadataClient nmc = new BeanConfiguration.NullCloudMetadataClient();
        assertFalse(nmc.supportsCloudMetadata());
        assertFalse(nmc.supportsTenantInstance());
        assertFalse(nmc.needsOBTenantName());
        assertFalse(nmc.needsSysTenantUser());
        assertTrue(nmc.includeClusterNameForJdbcUsername());
    }

    @Test(expected = UnsupportedException.class)
    public void testUnsupportedFunctionality() {
        BeanConfiguration.NullCloudMetadataClient nmc = new BeanConfiguration.NullCloudMetadataClient();
        nmc.listInstances();
        nmc.listTenants("instanceId");
        nmc.getTenant("instanceId", "tenantId");
        nmc.listDatabaseUsers("instanceId", "tenantId");
        nmc.getSysTenantUser("instanceId");
    }
    
}