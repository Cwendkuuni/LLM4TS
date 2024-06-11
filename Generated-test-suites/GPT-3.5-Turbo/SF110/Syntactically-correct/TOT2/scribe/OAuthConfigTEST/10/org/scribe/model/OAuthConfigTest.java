package org.scribe.model;
import static org.junit.Assert.*;
import org.junit.Test;

public class OAuthConfigTest {
    
    // Test case for constructor with only key and secret
    @Test
    public void testConstructorWithKeyAndSecret() {
        OAuthConfig config = new OAuthConfig("key1", "secret1");
        
        assertEquals("key1", config.getApiKey());
        assertEquals("secret1", config.getApiSecret());
        assertEquals("oob", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertNull(config.getScope());
        assertFalse(config.hasScope());
    }
    
    // Test case for constructor with all parameters
    @Test
    public void testFullConstructor() {
        OAuthConfig config = new OAuthConfig("key2", "secret2", "http://example.com/callback", SignatureType.QueryString, "email");
        
        assertEquals("key2", config.getApiKey());
        assertEquals("secret2", config.getApiSecret());
        assertEquals("http://example.com/callback", config.getCallback());
        assertEquals(SignatureType.QueryString, config.getSignatureType());
        assertEquals("email", config.getScope());
        assertTrue(config.hasScope());
    }
    
    // Test case for getApiKey method
    @Test
    public void testGetApiKey() {
        OAuthConfig config = new OAuthConfig("key3", "secret3");
        
        assertEquals("key3", config.getApiKey());
    }
    
    // Test case for getApiSecret method
    @Test
    public void testGetApiSecret() {
        OAuthConfig config = new OAuthConfig("key4", "secret4");
        
        assertEquals("secret4", config.getApiSecret());
    }
    
    // Test case for getCallback method
    @Test
    public void testGetCallback() {
        OAuthConfig config = new OAuthConfig("key5", "secret5", "http://example.com/callback", SignatureType.QueryString, null);
        
        assertEquals("http://example.com/callback", config.getCallback());
    }
    
    // Test case for getSignatureType method
    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("key6", "secret6");
        
        assertEquals(SignatureType.Header, config.getSignatureType());
    }
    
    // Test case for getScope method
    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("key7", "secret7", null, null, "email");
        
        assertEquals("email", config.getScope());
    }
    
    // Test case for hasScope method
    @Test
    public void testHasScopeTrue() {
        OAuthConfig config = new OAuthConfig("key8", "secret8", null, null, "email");
        
        assertTrue(config.hasScope());
    }
    
    // Test case for hasScope method
    @Test
    public void testHasScopeFalse() {
        OAuthConfig config = new OAuthConfig("key9", "secret9");
        
        assertFalse(config.hasScope());
    }
}