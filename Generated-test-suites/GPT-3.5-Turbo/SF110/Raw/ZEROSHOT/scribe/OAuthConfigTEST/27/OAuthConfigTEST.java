import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {

    @Test
    public void testGetApiKey() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertEquals("key", config.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertEquals("secret", config.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals("callback", config.getCallback());

        OAuthConfig config2 = new OAuthConfig("key", "secret");
        assertEquals("oob", config2.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals(SignatureType.Header, config.getSignatureType());

        OAuthConfig config2 = new OAuthConfig("key", "secret");
        assertEquals(SignatureType.Header, config2.getSignatureType());
    }

    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals("scope", config.getScope());

        OAuthConfig config2 = new OAuthConfig("key", "secret");
        assertNull(config2.getScope());
    }

    @Test
    public void testHasScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertTrue(config.hasScope());

        OAuthConfig config2 = new OAuthConfig("key", "secret");
        assertFalse(config2.hasScope());
    }
}