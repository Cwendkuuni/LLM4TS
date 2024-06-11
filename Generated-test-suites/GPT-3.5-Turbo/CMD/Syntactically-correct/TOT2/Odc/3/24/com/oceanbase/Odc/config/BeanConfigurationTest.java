import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "password123";
        String encodedPassword = encoder.encode(password);

        assertTrue(encoder.matches(password, encodedPassword));
    }

    @Test
    public void testObjectMapper() {
        // To be implemented
    }

    @Test
    public void testDefaultHttpFirewall() {
        // To be implemented
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient client = new BeanConfiguration.NullCloudMetadataClient();
        // To be implemented
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer serializer = new DefaultCookieSerializer();
        assertEquals("JSESSIONID", serializer.getCookieName());
    }
}