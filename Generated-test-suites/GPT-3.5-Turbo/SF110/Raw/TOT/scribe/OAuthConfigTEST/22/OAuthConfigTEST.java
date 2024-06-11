package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

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
    public void testGetCallback_DefaultValue() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertEquals("oob", config.getCallback());
    }
    
    @Test
    public void testGetCallback_CustomValue() {
        OAuthConfig config = new OAuthConfig("key", "secret", "https://example.com/callback", SignatureType.Header, null);
        assertEquals("https://example.com/callback", config.getCallback());
    }
    
    @Test
    public void testGetSignatureType_DefaultValue() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertEquals(SignatureType.Header, config.getSignatureType());
    }
    
    @Test
    public void testGetSignatureType_CustomValue() {
        OAuthConfig config = new OAuthConfig("key", "secret", null, SignatureType.QueryString, null);
        assertEquals(SignatureType.QueryString, config.getSignatureType());
    }
    
    @Test
    public void testGetScope_Null() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertNull(config.getScope());
    }
    
    @Test
    public void testGetScope_NotNull() {
        OAuthConfig config = new OAuthConfig("key", "secret", null, null, "read write");
        assertEquals("read write", config.getScope());
    }
    
    @Test
    public void testHasScope_True() {
        OAuthConfig config = new OAuthConfig("key", "secret", null, null, "read write");
        assertTrue(config.hasScope());
    }
    
    @Test
    public void testHasScope_False() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertFalse(config.hasScope());
    }
}