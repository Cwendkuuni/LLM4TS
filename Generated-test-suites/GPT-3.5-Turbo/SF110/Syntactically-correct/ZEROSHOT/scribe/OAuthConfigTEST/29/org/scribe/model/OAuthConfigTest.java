import org.junit.Test;
import static org.junit.Assert.*;

import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

    private final String apiKey = "API_KEY";
    private final String apiSecret = "API_SECRET";
    private final String callback = "CALLBACK";
    private final SignatureType signatureType = SignatureType.QueryString;
    private final String scope = "SCOPE";

    @Test
    public void testConstructorWithKeyAndSecret() {
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals("oob", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }

    @Test
    public void testConstructorWithAllParams() {
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        assertEquals(apiKey, config.getApiKey());
        assertEquals(apiSecret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(signatureType, config.getSignatureType());
        assertEquals(scope, config.getScope());
        assertTrue(config.hasScope());
    }

    @Test
    public void testGetApiKey() {
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertEquals(apiKey, config.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertEquals(apiSecret, config.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        assertEquals(callback, config.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        assertEquals(signatureType, config.getSignatureType());
    }

    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        assertEquals(scope, config.getScope());
    }

    @Test
    public void testHasScope() {
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope);
        assertTrue(config.hasScope());
    }

    @Test
    public void testHasNoScope() {
        OAuthConfig config = new OAuthConfig(apiKey, apiSecret);
        assertFalse(config.hasScope());
    }
}

