import static org.junit.Assert.*;
import org.junit.Test;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        // Typical case
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        assertNotNull(passwordEncoder.encode("password"));
    }

    @Test
    public void testObjectMapper() {
        // Typical case
        ObjectMapper objectMapper = new ObjectMapper(new SensitivePropertyHandler());
        assertNotNull(objectMapper.writeValueAsString(new Object()));

        // Edge case
        assertNull(objectMapper.writeValueAsString(null));
    }

    @Test
    public void testDefaultHttpFirewall() {
        // Typical case
        HttpFirewall httpFirewall = BeanConfiguration.defaultHttpFirewall();
        assertTrue(httpFirewall instanceof DefaultHttpFirewall);

        // Edge case
        assertNotNull(httpFirewall.getFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        // Typical case
        CloudMetadataClient cloudMetadataClient = new NullCloudMetadataClient();
        assertFalse(cloudMetadataClient.supportsCloudMetadata());

        // Error scenario
        try {
            cloudMetadataClient.listInstances();
            fail("Exception not thrown");
        } catch (UnsupportedException e) {
            assertEquals("CloudMetadata not supported", e.getMessage());
        }
    }

    @Test
    public void testCookieSerializer() {
        // Typical case
        CookieSerializer cookieSerializer = BeanConfiguration.cookieSerializer();
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }

}