package org.scribe.model;
import static org.junit.Assert.*;
import org.junit.Test;

public class OAuthConfigTest {
    
    @Test
    public void testGetApiKey() {
        OAuthConfig config = new OAuthConfig("key123", "secret456");
        assertEquals("key123", config.getApiKey());
    }
    
    @Test
    public void testGetApiSecret() {
        OAuthConfig config = new OAuthConfig("key123", "secret456");
        assertEquals("secret456", config.getApiSecret());
    }
    
    @Test
    public void testGetCallback() {
        OAuthConfig config = new OAuthConfig("key123", "secret456");
        assertEquals("oob", config.getCallback());
        
        OAuthConfig configWithCallback = new OAuthConfig("key123", "secret456", "http://example.com/callback", SignatureType.QueryString, "email");
        assertEquals("http://example.com/callback", configWithCallback.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("key123", "secret456");
        assertEquals(SignatureType.Header, config.getSignatureType());
        
        OAuthConfig configWithSignatureType = new OAuthConfig("key123", "secret456", null, SignatureType.QueryString, null);
        assertEquals(SignatureType.QueryString, configWithSignatureType.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("key123", "secret456");
        assertNull(config.getScope());
        
        OAuthConfig configWithScope = new OAuthConfig("key123", "secret456", null, null, "email");
        assertEquals("email", configWithScope.getScope());
    }
    
    @Test
    public void testHasScope() {
        OAuthConfig config = new OAuthConfig("key123", "secret456");
        assertFalse(config.hasScope());
        
        OAuthConfig configWithScope = new OAuthConfig("key123", "secret456", null, null, "email");
        assertTrue(configWithScope.hasScope());
    }
    
    // Edge case: Testing initialization with null callback and type
    @Test
    public void testConstructorWithNullCallbackAndType() {
        OAuthConfig config = new OAuthConfig("key123", "secret456", null, null, null);
        assertEquals("oob", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
    }
    
    // Exception handling: Testing initialization with null key and secret
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullKeyAndSecret() {
        OAuthConfig config = new OAuthConfig(null, null);
    }
}