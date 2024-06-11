package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

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
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals("callback", config.getCallback());
        
        OAuthConfig configWithNullCallback = new OAuthConfig("key", "secret");
        assertEquals("oob", configWithNullCallback.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals(SignatureType.Header, config.getSignatureType());
        
        OAuthConfig configWithNullSignatureType = new OAuthConfig("key", "secret");
        assertEquals(SignatureType.Header, configWithNullSignatureType.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals("scope", config.getScope());
        
        OAuthConfig configWithNullScope = new OAuthConfig("key", "secret");
        assertNull(configWithNullScope.getScope());
    }
    
    @Test
    public void testHasScope() {
        OAuthConfig configWithScope = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertTrue(configWithScope.hasScope());
        
        OAuthConfig configWithNullScope = new OAuthConfig("key", "secret");
        assertFalse(configWithNullScope.hasScope());
    }
}