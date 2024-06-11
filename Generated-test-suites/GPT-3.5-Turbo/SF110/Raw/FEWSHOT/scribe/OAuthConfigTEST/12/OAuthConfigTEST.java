import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class OAuthConfigTest {

    @Test
    public void testGetApiKey() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret");
        assertEquals("key", oauthConfig.getApiKey());
    }
    
    @Test
    public void testGetApiSecret() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret");
        assertEquals("secret", oauthConfig.getApiSecret());
    }
    
    @Test
    public void testGetCallback() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals("callback", oauthConfig.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals("scope", oauthConfig.getScope());
    }
    
    @Test
    public void testHasScope() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertTrue(oauthConfig.hasScope());
    }
    
    @Test
    public void testHasNoScope() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Header, null);
        assertFalse(oauthConfig.hasScope());
    }
}