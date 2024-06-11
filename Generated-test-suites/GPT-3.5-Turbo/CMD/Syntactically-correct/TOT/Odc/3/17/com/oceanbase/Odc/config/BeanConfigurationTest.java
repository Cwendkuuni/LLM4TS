import static org.junit.Assert.*;
import org.junit.Test;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        // Typical use case
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        assertNotNull(passwordEncoder);

        // Edge case
        assertNotEquals(passwordEncoder.encode("password123"), passwordEncoder.encode("password456"));
    }

    @Test
    public void testObjectMapper() {
        // Typical use case
        ObjectMapper objectMapper = new ObjectMapper(new SensitivePropertyHandler());
        assertNotNull(objectMapper);

        // Error scenario
        assertThrows(UnsupportedOperationException.class, () -> {
            objectMapper.readValue("invalid json", Object.class);
        });
    }

    @Test
    public void testDefaultHttpFirewall() {
        // Typical use case
        HttpFirewall httpFirewall = new DefaultHttpFirewall();
        assertTrue(httpFirewall instanceof DefaultHttpFirewall);

        // Edge case
        assertTrue(((DefaultHttpFirewall) httpFirewall).isAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        // Typical use case
        CloudMetadataClient cloudMetadataClient = new NullCloudMetadataClient();
        assertNotNull(cloudMetadataClient);

        // Error scenario
        assertThrows(UnsupportedException.class, () -> {
            cloudMetadataClient.listInstances();
        });
    }

    @Test
    public void testCookieSerializer() {
        // Typical use case
        CookieSerializer cookieSerializer = new DefaultCookieSerializer();
        assertNotNull(cookieSerializer);

        // Edge case
        cookieSerializer.setCookieName("TEST_COOKIE");
        assertEquals("TEST_COOKIE", cookieSerializer.getCookieName());
    }

}