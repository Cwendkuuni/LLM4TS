import static org.junit.Assert.*;
import org.junit.Test;

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
        OAuthConfig config = new OAuthConfig("key", "secret", "http://callback", SignatureType.QueryString, "scope");
        assertEquals("http://callback", config.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("key", "secret", "http://callback", SignatureType.QueryString, "scope");
        assertEquals(SignatureType.QueryString, config.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "http://callback", SignatureType.QueryString, "scope");
        assertEquals("scope", config.getScope());
    }
    
    @Test
    public void testHasScope() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertFalse(config.hasScope());
        
        OAuthConfig configWithScope = new OAuthConfig("key", "secret", "http://callback", SignatureType.QueryString, "scope");
        assertTrue(configWithScope.hasScope());
    }
}