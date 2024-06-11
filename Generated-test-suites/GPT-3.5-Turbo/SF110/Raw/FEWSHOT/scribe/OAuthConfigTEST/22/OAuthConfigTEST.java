package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class OAuthConfigTest {
    @Test
    public void testConstructorWithoutCallbackAndScope() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("oob", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertFalse(config.hasScope());
    }
    
    @Test
    public void testConstructorWithCallbackAndScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("callback", config.getCallback());
        assertEquals(SignatureType.QueryString, config.getSignatureType());
        assertEquals("scope", config.getScope());
        assertTrue(config.hasScope());
    }
    
    @Test
    public void testGetters() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("callback", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertEquals("scope", config.getScope());
        assertTrue(config.hasScope());
    }
}