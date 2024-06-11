import org.junit.Before;
import org.junit.Test;
import org.scribe.model.OAuthConfig;

import static org.junit.Assert.*;

public class OAuthConfigTest {
    
    private OAuthConfig oauthConfig;
    
    @Before
    public void setup() {
        oauthConfig = new OAuthConfig("api_key", "api_secret", "callback_url", SignatureType.Header, "scope");
    }
    
    @Test
    public void testGetApiKey() {
        assertEquals("api_key", oauthConfig.getApiKey());
    }
    
    @Test
    public void testGetApiSecret() {
        assertEquals("api_secret", oauthConfig.getApiSecret());
    }
    
    @Test
    public void testGetCallback() {
        assertEquals("callback_url", oauthConfig.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        assertEquals("scope", oauthConfig.getScope());
    }
    
    @Test
    public void testHasScope() {
        assertTrue(oauthConfig.hasScope());
    }
    
    @Test
    public void testCreationWithNullCallback() {
        OAuthConfig config = new OAuthConfig("api_key", "api_secret", null, SignatureType.Header, "scope");
        assertEquals("oob", config.getCallback());
    }
    
    @Test
    public void testCreationWithNullSignatureType() {
        OAuthConfig config = new OAuthConfig("api_key", "api_secret", "callback_url", null, "scope");
        assertEquals(SignatureType.Header, config.getSignatureType());
    }
    
    @Test
    public void testCreationWithNullScope() {
        OAuthConfig config = new OAuthConfig("api_key", "api_secret", "callback_url", SignatureType.Header, null);
        assertFalse(config.hasScope());
    }
    
}