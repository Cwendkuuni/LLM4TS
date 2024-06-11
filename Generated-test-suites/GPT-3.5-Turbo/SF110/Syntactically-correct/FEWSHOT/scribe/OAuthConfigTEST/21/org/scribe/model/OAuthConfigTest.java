package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OAuthConfigTest {
    @Test
    public void testGetApiKey() {
        OAuthConfig oAuthConfig = new OAuthConfig("key", "secret");
        assertEquals("key", oAuthConfig.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        OAuthConfig oAuthConfig = new OAuthConfig("key", "secret");
        assertEquals("secret", oAuthConfig.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        OAuthConfig oAuthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals("callback", oAuthConfig.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        OAuthConfig oAuthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
    }

    @Test
    public void testGetScope() {
        OAuthConfig oAuthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals("scope", oAuthConfig.getScope());
    }

    @Test
    public void testHasScopeWithScope() {
        OAuthConfig oAuthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Header, "scope");
        assertEquals(true, oAuthConfig.hasScope());
    }

    @Test
    public void testHasScopeWithoutScope() {
        OAuthConfig oAuthConfig = new OAuthConfig("key", "secret", "callback", SignatureType.Header, null);
        assertEquals(false, oAuthConfig.hasScope());
    }
}