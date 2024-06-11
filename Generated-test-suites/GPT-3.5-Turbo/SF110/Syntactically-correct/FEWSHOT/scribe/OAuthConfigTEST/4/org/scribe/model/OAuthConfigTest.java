package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertEquals("oob", config.getCallback());
        
        OAuthConfig configWithCallback = new OAuthConfig("key", "secret", "http://example.com/callback", null, null);
        assertEquals("http://example.com/callback", configWithCallback.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertEquals(SignatureType.Header, config.getSignatureType());
        
        OAuthConfig configWithSignatureType = new OAuthConfig("key", "secret", null, SignatureType.QueryString, null);
        assertEquals(SignatureType.QueryString, configWithSignatureType.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertNull(config.getScope());
        
        OAuthConfig configWithScope = new OAuthConfig("key", "secret", null, null, "scope");
        assertEquals("scope", configWithScope.getScope());
    }
    
    @Test
    public void testHasScope() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertTrue(!config.hasScope());
        
        OAuthConfig configWithScope = new OAuthConfig("key", "secret", null, null, "scope");
        assertTrue(configWithScope.hasScope());
    }
}