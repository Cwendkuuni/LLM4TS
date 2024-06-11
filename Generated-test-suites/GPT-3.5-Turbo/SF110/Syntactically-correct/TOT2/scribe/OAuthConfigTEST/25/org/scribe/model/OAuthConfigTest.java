package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testGetApiKey() {
        OAuthConfig config = new OAuthConfig("key123", "secret456");
        assertEquals("key123", config.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        OAuthConfig config = new OAuthConfig("key123", "secret456");
        assertEquals("secret456", config.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        OAuthConfig config = new OAuthConfig("key123", "secret456", "http://example.com/callback", SignatureType.QueryString, "read");
        assertEquals("http://example.com/callback", config.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("key123", "secret456", "http://example.com/callback", SignatureType.QueryString, "read");
        assertEquals(SignatureType.QueryString, config.getSignatureType());
    }

    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("key123", "secret456", "http://example.com/callback", SignatureType.QueryString, "read");
        assertEquals("read", config.getScope());
    }

    @Test
    public void testHasScopeWithScope() {
        OAuthConfig config = new OAuthConfig("key123", "secret456", "http://example.com/callback", SignatureType.QueryString, "read");
        assertTrue(config.hasScope());
    }

    @Test
    public void testHasScopeWithoutScope() {
        OAuthConfig config = new OAuthConfig("key123", "secret456", "http://example.com/callback", SignatureType.QueryString, null);
        assertFalse(config.hasScope());
    }
}