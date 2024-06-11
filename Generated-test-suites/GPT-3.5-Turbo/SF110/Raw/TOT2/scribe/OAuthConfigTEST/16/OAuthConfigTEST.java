import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {

    // Test cases for public methods in OAuthConfig class
    
    @Test
    public void testConstructorWithKeyAndSecret() {
        String key = "testKey";
        String secret = "testSecret";
        OAuthConfig config = new OAuthConfig(key, secret);
        assertEquals(key, config.getApiKey());
        assertEquals(secret, config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
    }
    
    @Test
    public void testConstructorWithAllParams() {
        String key = "testKey";
        String secret = "testSecret";
        String callback = "testCallback";
        SignatureType type = SignatureType.QueryString;
        String scope = "testScope";
        OAuthConfig config = new OAuthConfig(key, secret, callback, type, scope);
        assertEquals(key, config.getApiKey());
        assertEquals(secret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(type, config.getSignatureType());
        assertEquals(scope, config.getScope());
    }
    
    @Test
    public void testGetApiKey() {
        String key = "testKey";
        OAuthConfig config = new OAuthConfig(key, "testSecret");
        assertEquals(key, config.getApiKey());
    }
    
    @Test
    public void testGetApiSecret() {
        String secret = "testSecret";
        OAuthConfig config = new OAuthConfig("testKey", secret);
        assertEquals(secret, config.getApiSecret());
    }
    
    @Test
    public void testGetCallback() {
        String callback = "testCallback";
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", callback, SignatureType.QueryString, "testScope");
        assertEquals(callback, config.getCallback());
    }
    
    // Add more test cases for other methods in OAuthConfig class
    
    @Test
    public void testHasScopeWithNullScope() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret");
        assertFalse(config.hasScope());
    }
    
    @Test
    public void testHasScopeWithValidScope() {
        OAuthConfig config = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.Header, "testScope");
        assertTrue(config.hasScope());
    }
}