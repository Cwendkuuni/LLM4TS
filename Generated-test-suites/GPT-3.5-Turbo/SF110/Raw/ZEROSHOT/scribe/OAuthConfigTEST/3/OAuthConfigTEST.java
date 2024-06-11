package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {
    
    @Test
    public void testGetApiKey() {
        // create OAuthConfig instance
        OAuthConfig config = new OAuthConfig("key", "secret");
        
        // test getApiKey
        assertEquals("key", config.getApiKey());
    }
    
    @Test
    public void testGetApiSecret() {
        // create OAuthConfig instance
        OAuthConfig config = new OAuthConfig("key", "secret");
        
        // test getApiSecret
        assertEquals("secret", config.getApiSecret());
    }
    
    @Test
    public void testGetCallback() {
        // create OAuthConfig instance with callback
        OAuthConfig configWithCallback = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        
        // test getCallback
        assertEquals("callback", configWithCallback.getCallback());
        
        // create OAuthConfig instance without callback
        OAuthConfig configWithoutCallback = new OAuthConfig("key", "secret");
        
        // test getCallback
        assertEquals("oob", configWithoutCallback.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        // create OAuthConfig instance with signature type
        OAuthConfig configWithSignatureType = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        
        // test getSignatureType
        assertEquals(SignatureType.Header, configWithSignatureType.getSignatureType());
        
        // create OAuthConfig instance without signature type
        OAuthConfig configWithoutSignatureType = new OAuthConfig("key", "secret");
        
        // test getSignatureType
        assertEquals(SignatureType.Header, configWithoutSignatureType.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        // create OAuthConfig instance with scope
        OAuthConfig configWithScope = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        
        // test getScope
        assertEquals("scope", configWithScope.getScope());
        
        // create OAuthConfig instance without scope
        OAuthConfig configWithoutScope = new OAuthConfig("key", "secret");
        
        // test getScope
        assertNull(configWithoutScope.getScope());
    }
    
    @Test
    public void testHasScope() {
        // create OAuthConfig instance with scope
        OAuthConfig configWithScope = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        
        // test hasScope
        assertTrue(configWithScope.hasScope());
        
        // create OAuthConfig instance without scope
        OAuthConfig configWithoutScope = new OAuthConfig("key", "secret");
        
        // test hasScope
        assertFalse(configWithoutScope.hasScope());
    }
}