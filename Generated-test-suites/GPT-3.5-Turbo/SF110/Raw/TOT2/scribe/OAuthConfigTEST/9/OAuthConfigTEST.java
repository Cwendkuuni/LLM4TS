import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {
    
    @Test
    public void testOAuthConfigConstructor1() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }
    
    @Test
    public void testOAuthConfigConstructor2() {
        OAuthConfig config = new OAuthConfig("key", "secret", "http://example.com/callback", SignatureType.QueryString, "read write");
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("http://example.com/callback", config.getCallback());
        assertEquals(SignatureType.QueryString, config.getSignatureType());
        assertEquals("read write", config.getScope());
        assertTrue(config.hasScope());
    }
    
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
        OAuthConfig config = new OAuthConfig("key", "secret", "http://example.com/callback", SignatureType.QueryString, "read write");
        assertEquals("http://example.com/callback", config.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("key", "secret", "http://example.com/callback", SignatureType.QueryString, "read write");
        assertEquals(SignatureType.QueryString, config.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "http://example.com/callback", SignatureType.QueryString, "read write");
        assertEquals("read write", config.getScope());
    }
    
    @Test
    public void testHasScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "http://example.com/callback", SignatureType.QueryString, "read write");
        assertTrue(config.hasScope());
    }
}