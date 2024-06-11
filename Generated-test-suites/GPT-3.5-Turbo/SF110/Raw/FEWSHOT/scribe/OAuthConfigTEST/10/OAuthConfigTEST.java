package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class OAuthConfigTest {
    @Test
    public void testConstructor1() {
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret");
        
        assertEquals("apiKey", config.getApiKey());
        assertEquals("apiSecret", config.getApiSecret());
        assertNull(config.getCallback());
        assertNull(config.getSignatureType());
        assertNull(config.getScope());
    }
    
    @Test
    public void testConstructor2() {
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.Header, "scope");
        
        assertEquals("apiKey", config.getApiKey());
        assertEquals("apiSecret", config.getApiSecret());
        assertEquals("callback", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertEquals("scope", config.getScope());
    }
    
    @Test
    public void testConstructor2NullCheck() {
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", null, null, null);
        
        assertEquals("apiKey", config.getApiKey());
        assertEquals("apiSecret", config.getApiSecret());
        assertEquals("oob", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
    }
    
    @Test
    public void testGetApiKey() {
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret");
        
        assertEquals("apiKey", config.getApiKey());
    }
    
    @Test
    public void testGetApiSecret() {
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret");
        
        assertEquals("apiSecret", config.getApiSecret());
    }
    
    @Test
    public void testGetCallback() {
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.Header, "scope");
        
        assertEquals("callback", config.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.Header, "scope");
        
        assertEquals(SignatureType.Header, config.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.Header, "scope");
        
        assertEquals("scope", config.getScope());
    }
    
    @Test
    public void testHasScope() {
        OAuthConfig configWithScope = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.Header, "scope");
        OAuthConfig configWithoutScope = new OAuthConfig("apiKey", "apiSecret");
        
        assertTrue(configWithScope.hasScope());
        assertFalse(configWithoutScope.hasScope());
    }
}