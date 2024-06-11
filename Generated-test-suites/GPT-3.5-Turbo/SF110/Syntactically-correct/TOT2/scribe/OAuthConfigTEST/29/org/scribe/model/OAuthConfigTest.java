package org.scribe.model;
import static org.junit.Assert.*;
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
        OAuthConfig config = new OAuthConfig("key", "secret", "http://example.com", SignatureType.Header, "read write");
        assertEquals("http://example.com", config.getCallback());
    }
    
    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("key", "secret", "http://example.com", SignatureType.Header, "read write");
        assertEquals(SignatureType.Header, config.getSignatureType());
    }
    
    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "http://example.com", SignatureType.Header, "read write");
        assertEquals("read write", config.getScope());
    }

    @Test
    public void testHasScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "http://example.com", SignatureType.Header, "read write");
        assertTrue(config.hasScope());
    }

    @Test
    public void testNoScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "http://example.com", SignatureType.Header, null);
        assertFalse(config.hasScope());
    }
}