import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {
    
    @Test
    public void testConstructorWithTwoArguments() {
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret");
        
        assertEquals("apiKey", config.getApiKey());
        assertEquals("apiSecret", config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }
    
    @Test
    public void testConstructorWithAllArguments() {
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.QueryString, "scope");
        
        assertEquals("apiKey", config.getApiKey());
        assertEquals("apiSecret", config.getApiSecret());
        assertEquals("callback", config.getCallback());
        assertEquals(SignatureType.QueryString, config.getSignatureType());
        assertEquals("scope", config.getScope());
        assertTrue(config.hasScope());
    }
    
    @Test
    public void testConstructorWithNullCallback() {
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", null, SignatureType.QueryString, "scope");
        
        assertEquals("apiKey", config.getApiKey());
        assertEquals("apiSecret", config.getApiSecret());
        assertEquals("oob", config.getCallback());
        assertEquals(SignatureType.QueryString, config.getSignatureType());
        assertEquals("scope", config.getScope());
        assertTrue(config.hasScope());
    }
    
    @Test
    public void testConstructorWithNullSignatureType() {
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callback", null, "scope");
        
        assertEquals("apiKey", config.getApiKey());
        assertEquals("apiSecret", config.getApiSecret());
        assertEquals("callback", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertEquals("scope", config.getScope());
        assertTrue(config.hasScope());
    }
    
    @Test
    public void testConstructorWithNullScope() {
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.QueryString, null);
        
        assertEquals("apiKey", config.getApiKey());
        assertEquals("apiSecret", config.getApiSecret());
        assertEquals("callback", config.getCallback());
        assertEquals(SignatureType.QueryString, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }
}