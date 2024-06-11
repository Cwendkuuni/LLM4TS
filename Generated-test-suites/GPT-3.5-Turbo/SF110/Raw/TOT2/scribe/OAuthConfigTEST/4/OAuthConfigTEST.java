package org.scribe.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OAuthConfigTest {

    @Test
    public void testOAuthConfigConstructor1() {
        OAuthConfig config = new OAuthConfig("key","secret");
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals(null, config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertEquals(null, config.getScope());
    }

    @Test
    public void testOAuthConfigConstructor2() {
        OAuthConfig config = new OAuthConfig("key","secret","callback",SignatureType.QueryString,"scope");
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("callback", config.getCallback());
        assertEquals(SignatureType.QueryString, config.getSignatureType());
        assertEquals("scope", config.getScope());
    }

    @Test
    public void testGetApiKey() {
        OAuthConfig config = new OAuthConfig("key","secret");
        assertEquals("key", config.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        OAuthConfig config = new OAuthConfig("key","secret");
        assertEquals("secret", config.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        OAuthConfig config = new OAuthConfig("key","secret");
        assertEquals(null, config.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("key","secret");
        assertEquals(SignatureType.Header, config.getSignatureType());
    }

    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("key","secret");
        assertEquals(null, config.getScope());
    }

    @Test
    public void testHasScope() {
        OAuthConfig config = new OAuthConfig("key","secret");
        assertEquals(false, config.hasScope());
    }

    @Test
    public void testHasScopeWithScope() {
        OAuthConfig config = new OAuthConfig("key","secret","callback",SignatureType.QueryString,"scope");
        assertEquals(true, config.hasScope());
    }
}