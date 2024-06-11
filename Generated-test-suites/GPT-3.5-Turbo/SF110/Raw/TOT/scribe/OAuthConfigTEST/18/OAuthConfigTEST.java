import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {

    // Test cases for the first constructor
    @Test
    public void testFirstConstructor() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertNull(config.getCallback());
        assertNull(config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    // Test cases for the second constructor
    @Test
    public void testSecondConstructor() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("callback", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertEquals("scope", config.getScope());
        assertTrue(config.hasScope());
    }

    // Test cases for the second constructor with null values
    @Test
    public void testSecondConstructorWithNullValues() {
        OAuthConfig config = new OAuthConfig("key", "secret", null, null, null);
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("oob", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    // Edge case: Empty key and secret
    @Test
    public void testEmptyKeyAndSecret() {
        OAuthConfig config = new OAuthConfig("", "");
        assertEquals("", config.getApiKey());
        assertEquals("", config.getApiSecret());
        assertNull(config.getCallback());
        assertNull(config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    // Edge case: Empty values for callback, type, and scope
    @Test
    public void testEmptyValuesForCallbackTypeAndScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "", null, "");
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertEquals("", config.getScope());
        assertFalse(config.hasScope());
    }

    // Error scenario: Null key and secret
    @Test(expected = IllegalArgumentException.class)
    public void testNullKeyAndSecret() {
        new OAuthConfig(null, null);
    }
}