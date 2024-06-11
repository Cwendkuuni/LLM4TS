import org.junit.Test;
import static org.junit.Assert.*;

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
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret");
        assertEquals("oob", oauthConfig.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret");
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals("scope", oauthConfig.getScope());
    }
    
    @Test
    public void testHasScope() {
        OAuthConfig oauthConfig1 = new OAuthConfig("key", "secret");
        assertFalse(oauthConfig1.hasScope());
        
        OAuthConfig oauthConfig2 = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertTrue(oauthConfig2.hasScope());
    }
}