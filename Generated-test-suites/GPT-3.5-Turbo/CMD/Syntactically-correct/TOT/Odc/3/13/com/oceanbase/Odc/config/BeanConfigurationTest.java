import static org.junit.Assert.*;
import org.junit.Test;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        // Typical use case:
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        assertNotNull(passwordEncoder.encode("password123"));
        
        // Edge case:
        assertEquals("$2a$10$8U.F1DajNbXI1e53f85AyO9s4ZmVWQTsFTLXZXZ5GiB4vNc7pKtfW", passwordEncoder.encode("password123"));
        
        // Error scenario:
        assertNull(passwordEncoder.encode(null));
    }

    @Test
    public void testObjectMapper() {
        // Typical use case:
        ObjectMapper objectMapper = new ObjectMapper(new SensitivePropertyHandler());
        assertNotNull(objectMapper.writeValueAsString(new SomeObject()));
        
        // Edge case:
        assertEquals("{\"key\":\"value\"}", objectMapper.writeValueAsString(new SomeObject()));
        
        // Error scenario:
        assertNull(objectMapper.writeValueAsString(null));
    }

    @Test
    public void testDefaultHttpFirewall() {
        // Typical use case:
        HttpFirewall httpFirewall = new DefaultHttpFirewall();
        assertTrue(httpFirewall.getFirewalledRequest(new MockHttpServletRequest(), new MockHttpServletResponse()) instanceof FirewalledServletRequest);
        
        // Edge case:
        assertTrue(httpFirewall.getFirewalledRequest(new MockHttpServletRequest(), new MockHttpServletResponse()) instanceof FirewalledServletRequest);
        
        // Error scenario:
        assertNull(httpFirewall.getFirewalledRequest(null, null));
    }

    @Test
    public void testNullCloudMetadataClient() {
        // Typical use case:
        NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
        assertTrue(nullCloudMetadataClient.supportsCloudMetadata());
        
        // Edge case:
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
        
        // Error scenario:
        try {
            nullCloudMetadataClient.listInstances();
            fail("Expected UnsupportedException to be thrown");
        } catch (UnsupportedException e) {
            // Success
        }
    }
}