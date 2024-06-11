package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class OAuthConfigTest {
    @Test
    public void testConstructorWithTwoArguments() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("oob", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        
        assertFalse(config.hasScope());
        assertEquals(null, config.getScope());
    }
    
    @Test
    public void testConstructorWithFiveArguments() {
        SignatureType type = SignatureType.QueryString;
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", type, "scope");
        
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("callback", config.getCallback());
        assertEquals(type, config.getSignatureType());
        
        assertEquals(true, config.hasScope());
        assertEquals("scope", config.getScope());
    }
}