package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class OAuthConfigTest {
    @Test
    public void testConstructorWithTwoArguments() {
        OAuthConfig config = new OAuthConfig("key", "secret");

        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertNotNull(config.getCallback());
        assertNotNull(config.getSignatureType());
        assertFalse(config.hasScope());
    }

    @Test
    public void testConstructorWithAllArguments() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");

        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("callback", config.getCallback());
        assertEquals(SignatureType.QueryString, config.getSignatureType());
        assertEquals("scope", config.getScope());
        assertTrue(config.hasScope());
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
        OAuthConfig config = new OAuthConfig("key", "secret");

        assertNotNull(config.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        OAuthConfig config = new OAuthConfig("key", "secret");

        assertNotNull(config.getSignatureType());
    }

    @Test
    public void testGetScope() {
        OAuthConfig config = new OAuthConfig("key", "secret");

        assertNull(config.getScope());
    }

    @Test
    public void testHasScope() {
        OAuthConfig config = new OAuthConfig("key", "secret");

        assertFalse(config.hasScope());
    }
}