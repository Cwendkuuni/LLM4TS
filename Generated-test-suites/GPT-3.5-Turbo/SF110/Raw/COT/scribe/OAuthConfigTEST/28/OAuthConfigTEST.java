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
    public void testGetCallback() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertEquals("oob", config.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertEquals(SignatureType.Header, config.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertNull(config.getScope());
    }
    
    @Test
    public void testHasScope() {
        OAuthConfig configWithoutScope = new OAuthConfig("key", "secret");
        assertFalse(configWithoutScope.hasScope());
        
        OAuthConfig configWithScope = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertTrue(configWithScope.hasScope());
    }
    
    @Test
    public void testConstructorWithNullCallback() {
        OAuthConfig config = new OAuthConfig("key", "secret", null, SignatureType.Header, "scope");
        assertEquals("oob", config.getCallback());
    }
    
    @Test
    public void testConstructorWithNullSignatureType() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", null, "scope");
        assertEquals(SignatureType.Header, config.getSignatureType());
    }
    
    @Test
    public void testConstructorWithNullScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, null);
        assertNull(config.getScope());
    }

}