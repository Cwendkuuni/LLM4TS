import static org.junit.Assert.*;
import org.junit.Test;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        // typical use case
        assertNotNull(new BeanConfiguration().passwordEncoder());
    }

    @Test
    public void testObjectMapper() {
        // typical use case
        assertNotNull(new BeanConfiguration().objectMapper(null));
    }

    @Test
    public void testDefaultHttpFirewall() {
        // typical use case
        assertNotNull(new BeanConfiguration().defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        // typical use case
        assertNotNull(new BeanConfiguration().cloudMetadataClient());
        // edge case
        assertTrue(new BeanConfiguration().cloudMetadataClient().supportsCloudMetadata());
        // error scenario
        try {
            new BeanConfiguration().cloudMetadataClient().listInstances();
            fail("Exception not thrown");
        } catch (UnsupportedException e) {
            // Exception expected
        }
    }

    @Test
    public void testCookieSerializer() {
        // typical use case
        assertNotNull(new BeanConfiguration().cookieSerializer());
    }

    @Test
    public void testNullCloudMetadataClient() {
        // typical use case
        BeanConfiguration.NullCloudMetadataClient client = new BeanConfiguration.NullCloudMetadataClient();
        client.init();
        // edge case
        assertFalse(client.supportsCloudMetadata());
        // error scenario
        try {
            client.listTenants("1");
            fail("Exception not thrown");
        } catch (UnsupportedException e) {
            // Exception expected
        }
    }
}