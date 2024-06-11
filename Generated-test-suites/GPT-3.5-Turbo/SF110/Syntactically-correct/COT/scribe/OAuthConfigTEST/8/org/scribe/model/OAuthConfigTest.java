import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

    @Test
    public void testConstructorWithKeyAndSecret() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertSame("key", config.getApiKey());
        assertSame("secret", config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
    }

    @Test
    public void testConstructorWithAllParams() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Body, "scope");
        assertSame("key", config.getApiKey());
        assertSame("secret", config.getApiSecret());
        assertSame("callback", config.getCallback());
        assertEquals(SignatureType.Body, config.getSignatureType());
        assertSame("scope", config.getScope());
    }

    @Test
    public void testGetApiKey() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertSame("key", config.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertSame("secret", config.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Body, "scope");
        assertSame("callback", config.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Body, "scope");
        assertEquals(SignatureType.Body, config.getSignatureType());
    }

    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Body, "scope");
        assertSame("scope", config.getScope());
    }

    @Test
    public void testHasScope() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertFalse(config.hasScope());
    }
}