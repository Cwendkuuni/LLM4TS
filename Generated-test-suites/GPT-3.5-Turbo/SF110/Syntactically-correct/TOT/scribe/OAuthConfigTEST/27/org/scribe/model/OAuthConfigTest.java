import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {

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
    public void testConstructorWithNullCallback() {
        String key = "testKey";
        String secret = "testSecret";
        SignatureType type = SignatureType.Header;
        String scope = "testScope";
        OAuthConfig config = new OAuthConfig(key, secret, null, type, scope);
        assertEquals(key, config.getApiKey());
        assertEquals(secret, config.getApiSecret());
        assertEquals("oob", config.getCallback());
        assertEquals(type, config.getSignatureType());
        assertEquals(scope, config.getScope());
    }
    
    @Test
    public void testConstructorWithNullType() {
        String key = "testKey";
        String secret = "testSecret";
        String callback = "testCallback";
        String scope = "testScope";
        OAuthConfig config = new OAuthConfig(key, secret, callback, null, scope);
        assertEquals(key, config.getApiKey());
        assertEquals(secret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertEquals(scope, config.getScope());
    }
    
    @Test
    public void testConstructorWithNullScope() {
        String key = "testKey";
        String secret = "testSecret";
        String callback = "testCallback";
        SignatureType type = SignatureType.Header;
        OAuthConfig config = new OAuthConfig(key, secret, callback, type, null);
        assertEquals(key, config.getApiKey());
        assertEquals(secret, config.getApiSecret());
        assertEquals(callback, config.getCallback());
        assertEquals(type, config.getSignatureType());
        assertNull(config.getScope());
    }
    
    @Test
    public void testGetApiKey() {
        String key = "testKey";
        String secret = "testSecret";
        OAuthConfig config = new OAuthConfig(key, secret);
        assertEquals(key, config.getApiKey());
    }
    
    @Test
    public void testGetApiSecret() {
        String key = "testKey";
        String secret = "testSecret";
        OAuthConfig config = new OAuthConfig(key, secret);
        assertEquals(secret, config.getApiSecret());
    }
    
    @Test
    public void testGetCallback() {
        String key = "testKey";
        String secret = "testSecret";
        String callback = "testCallback";
        OAuthConfig config = new OAuthConfig(key, secret, callback, null, null);
        assertEquals(callback, config.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        String key = "testKey";
        String secret = "testSecret";
        SignatureType type = SignatureType.Header;
        OAuthConfig config = new OAuthConfig(key, secret, null, type, null);
        assertEquals(type, config.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        String key = "testKey";
        String secret = "testSecret";
        String callback = "testCallback";
        String scope = "testScope";
        OAuthConfig config = new OAuthConfig(key, secret, callback, null, scope);
        assertEquals(scope, config.getScope());
    }
    
    @Test
    public void testHasScopeWithNullScope() {
        String key = "testKey";
        String secret = "testSecret";
        OAuthConfig config = new OAuthConfig(key, secret);
        assertFalse(config.hasScope());
    }
    
    @Test
    public void testHasScopeWithNonNullScope() {
        String key = "testKey";
        String secret = "testSecret";
        String scope = "testScope";
        OAuthConfig config = new OAuthConfig(key, secret, null, null, scope);
        assertTrue(config.hasScope());
    }
}