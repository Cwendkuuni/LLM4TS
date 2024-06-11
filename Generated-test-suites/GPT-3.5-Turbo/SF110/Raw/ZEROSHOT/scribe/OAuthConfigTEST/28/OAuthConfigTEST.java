package org.scribe.model;

import org.junit.Test;

import static org.junit.Assert.*;

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
        
        OAuthConfig configWithCallback = new OAuthConfig("key123", "secret456", "http://example.com/callback", null, null);
        assertEquals("http://example.com/callback", configWithCallback.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("key123", "secret456");
        assertEquals(SignatureType.Header, config.getSignatureType());
        
        OAuthConfig configWithType = new OAuthConfig("key123", "secret456", null, SignatureType.QueryString, null);
        assertEquals(SignatureType.QueryString, configWithType.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("key123", "secret456");
        assertNull(config.getScope());
        
        OAuthConfig configWithScope = new OAuthConfig("key123", "secret456", null, null, "read write");
        assertEquals("read write", configWithScope.getScope());
    }
    
    @Test
    public void testHasScope() {
        OAuthConfig configNoScope = new OAuthConfig("key123", "secret456");
        assertFalse(configNoScope.hasScope());
        
        OAuthConfig configWithScope = new OAuthConfig("key123", "secret456", null, null, "read write");
        assertTrue(configWithScope.hasScope());
    }
}