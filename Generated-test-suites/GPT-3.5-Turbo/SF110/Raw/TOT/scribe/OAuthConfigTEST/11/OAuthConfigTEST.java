import org.junit.Test;
import static org.junit.Assert.*;

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
    public void testGetCallbackWithNonNullCallback() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals("callback", config.getCallback());
    }
    
    @Test
    public void testGetCallbackWithNullCallback() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertEquals("oob", config.getCallback());
    }
    
    @Test
    public void testGetSignatureTypeWithNonNullType() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals(SignatureType.Header, config.getSignatureType());
    }
    
    @Test
    public void testGetSignatureTypeWithNullType() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertEquals(SignatureType.Header, config.getSignatureType());
    }
    
    @Test
    public void testGetScopeWithNonNullScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals("scope", config.getScope());
    }
    
    @Test
    public void testGetScopeWithNullScope() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertNull(config.getScope());
    }
    
    @Test
    public void testHasScopeWithNonNullScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertTrue(config.hasScope());
    }
    
    @Test
    public void testHasScopeWithNullScope() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertFalse(config.hasScope());
    }
}