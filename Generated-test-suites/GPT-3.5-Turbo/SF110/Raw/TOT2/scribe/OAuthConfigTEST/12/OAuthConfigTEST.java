
import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {
  
    @Test
    public void testConstructorWithKeyAndSecret() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret");
        assertEquals("key", oauthConfig.getApiKey());
        assertEquals("secret", oauthConfig.getApiSecret());
        assertNull(oauthConfig.getCallback());
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
        assertNull(oauthConfig.getScope());
    }
    
    @Test
    public void testFullConstructor() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertEquals("key", oauthConfig.getApiKey());
        assertEquals("secret", oauthConfig.getApiSecret());
        assertEquals("callback", oauthConfig.getCallback());
        assertEquals(SignatureType.QueryString, oauthConfig.getSignatureType());
        assertEquals("scope", oauthConfig.getScope());
    }
    
    @Test
    public void testHasScope() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret");
        assertFalse(oauthConfig.hasScope());
        OAuthConfig oauthConfigWithScope = new OAuthConfig("key", "secret", null, null, "scope");
        assertTrue(oauthConfigWithScope.hasScope());
    }
}