
import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {

    @Test
    public void testGetApiKey() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        String apiKey = config.getApiKey();
        assertEquals("key", apiKey);
    }

    @Test
    public void testGetApiSecret() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        String apiSecret = config.getApiSecret();
        assertEquals("secret", apiSecret);
    }

    @Test
    public void testGetCallback() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        String callback = config.getCallback();
        assertEquals("callback", callback);
    }

    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        SignatureType signatureType = config.getSignatureType();
        assertEquals(SignatureType.Header, signatureType);
    }

    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        String scope = config.getScope();
        assertEquals("scope", scope);
    }

    @Test
    public void testHasScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        boolean hasScope = config.hasScope();
        assertTrue(hasScope);
    }
}
