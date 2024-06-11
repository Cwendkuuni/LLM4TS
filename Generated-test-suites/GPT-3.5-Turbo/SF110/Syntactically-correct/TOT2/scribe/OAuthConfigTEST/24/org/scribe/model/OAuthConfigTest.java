package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {

    // Test cases for the constructor with two arguments
    @Test
    public void testConstructorWithTwoArgs() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertNull(config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }
    
    // Test cases for the constructor with five arguments
    @Test
    public void testConstructorWithFiveArgs() {
        OAuthConfig config = new OAuthConfig("key", "secret", "http://example.com/callback", SignatureType.QueryString, "read write");
        
        assertEquals("key", config.getApiKey()); 
        assertEquals("secret", config.getApiSecret()); 
        assertEquals("http://example.com/callback", config.getCallback());
        assertEquals(SignatureType.QueryString, config.getSignatureType()); 
        assertEquals("read write", config.getScope()); 
        assertTrue(config.hasScope());
    }
    
    // Test cases for getting API Key
    @Test
    public void testGetApiKey() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        
        assertEquals("key", config.getApiKey());
    }
    
    // Test cases for getting API Secret
    @Test
    public void testGetApiSecret() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        
        assertEquals("secret", config.getApiSecret());
    }
    
    // Test cases for getting Callback
    @Test
    public void testGetCallback() {
        OAuthConfig config = new OAuthConfig("key", "secret", "http://example.com/callback", SignatureType.QueryString, "read write");
        
        assertEquals("http://example.com/callback", config.getCallback());
    }
    
    // Test cases for getting Signature Type
    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("key", "secret", "http://example.com/callback", SignatureType.QueryString, "read write");
        
        assertEquals(SignatureType.QueryString, config.getSignatureType());
    }
    
    // Test cases for getting Scope
    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "http://example.com/callback", SignatureType.QueryString, "read write");
        
        assertEquals("read write", config.getScope());
    }
    
    // Test cases for checking the existence of Scope
    @Test
    public void testHasScope() {
        OAuthConfig configWithScope = new OAuthConfig("key", "secret", "http://example.com/callback", SignatureType.QueryString, "read write");
        OAuthConfig configWithoutScope = new OAuthConfig("key", "secret");
        
        assertTrue(configWithScope.hasScope());
        assertFalse(configWithoutScope.hasScope());
    }

}