package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class OAuthConfigTest {
  
    @Test
    public void testConstructorWithKeyAndSecret() {
        OAuthConfig config = new OAuthConfig("key", "secret");
        assertNull(config.getCallback());
        assertNotNull(config.getSignatureType());
        assertNull(config.getScope());
    }
    
    @Test
    public void testConstructorWithAllParams() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertNotNull(config.getCallback());
        assertNotNull(config.getSignatureType());
        assertNotNull(config.getScope());
    }
    
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
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals("callback", config.getCallback());
    }
  
    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals(SignatureType.Header, config.getSignatureType());
    }
  
    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals("scope", config.getScope());
    }
  
    @Test
    public void testHasScope() {
        OAuthConfig config1 = new OAuthConfig("key", "secret");
        OAuthConfig config2 = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals(false, config1.hasScope());
        assertEquals(true, config2.hasScope());
    }
}