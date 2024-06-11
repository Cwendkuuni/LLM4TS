import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {
    
    @Test
    public void testConstructorWithTwoParams() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
    }
    
    @Test
    public void testConstructorWithAllParams() {
        OAuthConfig config = new OAuthConfig("key", "secret", "http://example.com/callback", SignatureType.Querystring, "read write");
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("http://example.com/callback", config.getCallback());
        assertEquals(SignatureType.Querystring, config.getSignatureType());
        assertEquals("read write", config.getScope());
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
        OAuthConfig config = new OAuthConfig("key", "secret", "http://example.com/callback", SignatureType.Querystring, "read write");
        assertEquals("http://example.com/callback", config.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("key", "secret", "http://example.com/callback", SignatureType.Querystring, "read write");
        assertEquals(SignatureType.Querystring, config.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "http://example.com/callback", SignatureType.Querystring, "read write");
        assertEquals("read write", config.getScope());
    }
    
    @Test
    public void testHasScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "http://example.com/callback", SignatureType.Querystring, "read write");
        assertTrue(config.hasScope());
    }
    
    @Test
    public void testHasNoScope() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertFalse(config.hasScope());
    }
    
}