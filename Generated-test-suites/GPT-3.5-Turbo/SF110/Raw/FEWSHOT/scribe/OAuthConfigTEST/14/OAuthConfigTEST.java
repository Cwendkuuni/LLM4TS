package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OAuthConfigTest {
    
    @Test
    public void testConstructorWith2Arguments() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        
        assertNotNull(config);
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertNull(config.getCallback());
        assertNull(config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }
    
    @Test
    public void testConstructorWith5Arguments() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        
        assertNotNull(config);
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("callback", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertEquals("scope", config.getScope());
        assertTrue(config.hasScope());
    }
    
    @Test
    public void testConstructorWithNullCallback() {
        OAuthConfig config = new OAuthConfig("key", "secret", null, SignatureType.Header, "scope");
        
        assertNotNull(config);
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("oob", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertEquals("scope", config.getScope());
        assertTrue(config.hasScope());
    }
    
    @Test
    public void testConstructorWithNullType() {
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
    public void testConstructorWithNullScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, null);
        
        assertNotNull(config);
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("callback", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }
}