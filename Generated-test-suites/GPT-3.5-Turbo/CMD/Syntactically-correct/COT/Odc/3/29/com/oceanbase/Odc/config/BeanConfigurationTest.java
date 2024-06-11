
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

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
        assertNotNull(beanConfiguration.objectMapper(new SensitivePropertyHandler()));
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
        assertFalse(nullCloudMetadataClient.needsOBTenantName());
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals("DIRECT", nullCloudMetadataClient.oceanbaseAccessMode().toString());
        
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

        assertNotNull(nullCloudMetadataClient.getTenantEndpoint("123", "456"));

        try {
            nullCloudMetadataClient.getSysTenantUser("123");
            fail("Expected UnsupportedException");
        } catch (UnsupportedException e) {
            // Expected
        }
    }

}
