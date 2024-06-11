
import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {
    
    @Test
    public void testConstructor() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertNotNull(config);
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("oob", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }
    
    @Test
    public void testConstructorWithOptionalValues() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertNotNull(config);
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("callback", config.getCallback());
        assertEquals(SignatureType.QueryString, config.getSignatureType());
        assertEquals("scope", config.getScope());
        assertTrue(config.hasScope());
    }
    
    @Test
    public void testConstructorWithNullCallback() {
        OAuthConfig config = new OAuthConfig("key", "secret", null, SignatureType.QueryString, "scope");
        assertNotNull(config);
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("oob", config.getCallback());
        assertEquals(SignatureType.QueryString, config.getSignatureType());
        assertEquals("scope", config.getScope());
        assertTrue(config.hasScope());
    }
    
    @Test
    public void testConstructorWithNullSignatureType() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", null, "scope");
        assertNotNull(config);
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("callback", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertEquals("scope", config.getScope());
        assertTrue(config.hasScope());
    }
    
    @Test
    public void testHasScopeWithScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertTrue(config.hasScope());
    }
    
    @Test
    public void testHasScopeWithoutScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, null);
        assertFalse(config.hasScope());
    }
}
