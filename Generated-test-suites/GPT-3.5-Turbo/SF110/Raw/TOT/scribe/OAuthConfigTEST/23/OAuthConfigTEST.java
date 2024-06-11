import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {

    @Test
    public void testConstructorWithKeyAndSecret() {
        String key = "KEY";
        String secret = "SECRET";

        OAuthConfig config = new OAuthConfig(key, secret);

        assertEquals(key, config.getApiKey());
        assertEquals(secret, config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
    }

    @Test
    public void testConstructorWithAllParameters() {
        String key = "KEY";
        String secret = "SECRET";
        String callback = "CALLBACK";
        SignatureType type = SignatureType.QueryString;
        String scope = "SCOPE";

        OAuthConfig config = new OAuthConfig(key, secret, callback, type, scope);

        assertEquals(key, config.getApiKey());
        assertEquals(secret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(type, config.getSignatureType());
        assertEquals(scope, config.getScope());
    }

    @Test
    public void testConstructorWithNullCallbackAndType() {
        String key = "KEY";
        String secret = "SECRET";

        OAuthConfig config = new OAuthConfig(key, secret, null, null, null);

        assertEquals(key, config.getApiKey());
        assertEquals(secret, config.getApiSecret());
        assertEquals("oob", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
    }

    @Test
    public void testGetApiKey() {
        String key = "KEY";
        String secret = "SECRET";

        OAuthConfig config = new OAuthConfig(key, secret);

        assertEquals(key, config.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        String key = "KEY";
        String secret = "SECRET";

        OAuthConfig config = new OAuthConfig(key, secret);

        assertEquals(secret, config.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        String key = "KEY";
        String secret = "SECRET";

        OAuthConfig config = new OAuthConfig(key, secret);

        assertNull(config.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        String key = "KEY";
        String secret = "SECRET";

        OAuthConfig config = new OAuthConfig(key, secret);

        assertEquals(SignatureType.Header, config.getSignatureType());
    }

    @Test
    public void testGetScope() {
        String key = "KEY";
        String secret = "SECRET";

        OAuthConfig config = new OAuthConfig(key, secret);

        assertNull(config.getScope());
    }

    @Test
    public void testHasScope() {
        String key = "KEY";
        String secret = "SECRET";

        OAuthConfig config = new OAuthConfig(key, secret);

        assertFalse(config.hasScope());
    }
}