import static org.junit.Assert.*;
import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfig() {
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret");
        assertEquals("apiKey", config.getApiKey());
        assertEquals("apiSecret", config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
    }
    
    @Test
    public void testOAuthConfigWithCallback() {
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.QueryString, "scope");
        assertEquals("apiKey", config.getApiKey());
        assertEquals("apiSecret", config.getApiSecret());
        assertEquals("callback", config.getCallback());
        assertEquals(SignatureType.QueryString, config.getSignatureType());
        assertEquals("scope", config.getScope());
    }
    
    @Test
    public void testOAuthConfigWithNullCallback() {
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", null, SignatureType.QueryString, "scope");
        assertEquals("apiKey", config.getApiKey());
        assertEquals("apiSecret", config.getApiSecret());
        assertEquals("oob", config.getCallback());
        assertEquals(SignatureType.QueryString, config.getSignatureType());
        assertEquals("scope", config.getScope());
    }
    
    @Test
    public void testGetApiKey() {
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret");
        assertEquals("apiKey", config.getApiKey());
    }
    
    @Test
    public void testGetApiSecret() {
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret");
        assertEquals("apiSecret", config.getApiSecret());
    }
    
    @Test
    public void testGetCallback() {
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.QueryString, "scope");
        assertEquals("callback", config.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.QueryString, "scope");
        assertEquals(SignatureType.QueryString, config.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.QueryString, "scope");
        assertEquals("scope", config.getScope());
    }
    
    @Test
    public void testHasScope() {
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.QueryString, "scope");
        assertTrue(config.hasScope());
    }
    
    @Test
    public void testHasNoScope() {
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret");
        assertFalse(config.hasScope());
    }
}