package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testGetApiKey() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret");
        assertEquals("key", oauthConfig.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret");
        assertEquals("secret", oauthConfig.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals("callback", oauthConfig.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
    }

    @Test
    public void testGetScope() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals("scope", oauthConfig.getScope());
    }

    @Test
    public void testHasScopeWithScope() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertTrue(oauthConfig.hasScope());
    }

    @Test
    public void testHasScopeWithoutScope() {
        OAuthConfig oauthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Header, null);
        assertFalse(oauthConfig.hasScope());
    }
}