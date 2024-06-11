package org.scribe.model;
import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {
    
    // Test cases for constructor with key and secret
    @Test
    public void testConstructorWithKeyAndSecret() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("oob", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
    }
    
    // Test cases for constructor with key, secret, callback, signature type, and scope
    @Test
    public void testConstructorWithAllParameters() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("callback", config.getCallback());
        assertEquals(SignatureType.QueryString, config.getSignatureType());
        assertEquals("scope", config.getScope());
    }
    
    // Test cases for getApiKey
    @Test
    public void testGetApiKey() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        
        assertEquals("key", config.getApiKey());
    }
    
    // Test cases for getApiSecret
    @Test
    public void testGetApiSecret() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        
        assertEquals("secret", config.getApiSecret());
    }
    
    // Test cases for getCallback
    @Test
    public void testGetCallback() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        
        assertEquals("oob", config.getCallback()); // Default value is "oob" if not provided
    }
    
    // Test cases for getSignatureType
    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        
        assertEquals(SignatureType.Header, config.getSignatureType()); // Default value is Header if not provided
    }
    
    // Test cases for getScope
    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        
        assertNull(config.getScope()); // Scope is null if not provided
    }
    
    // Test cases for hasScope
    @Test
    public void testHasScope() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        
        assertFalse(config.hasScope()); // Scope is null so should return false
    }
}