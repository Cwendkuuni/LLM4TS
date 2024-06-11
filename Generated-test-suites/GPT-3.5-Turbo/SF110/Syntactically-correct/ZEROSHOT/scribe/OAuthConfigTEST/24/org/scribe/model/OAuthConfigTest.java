package org.scribe.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OAuthConfigTest {

    private OAuthConfig config;

    @Before
    public void setUp() {
        config = new OAuthConfig("key", "secret");
    }

    @Test
    public void testGetApiKey() {
        assertEquals("key", config.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        assertEquals("secret", config.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        assertEquals("oob", config.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        assertEquals(SignatureType.Header, config.getSignatureType());
    }

    @Test
    public void testGetScope() {
        assertNull(config.getScope());
    }

    @Test
    public void testHasScope() {
        assertFalse(config.hasScope());
    }

    @Test
    public void testConstructorWithCallbackAndTypeAndScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, "scope");
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("callback", config.getCallback());
        assertEquals(SignatureType.QueryString, config.getSignatureType());
        assertEquals("scope", config.getScope());
    }

    @Test
    public void testConstructorWithNullCallback() {
        OAuthConfig config = new OAuthConfig("key", "secret", null, SignatureType.QueryString, "scope");
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("oob", config.getCallback());
        assertEquals(SignatureType.QueryString, config.getSignatureType());
        assertEquals("scope", config.getScope());
    }

    @Test
    public void testConstructorWithNullSignatureType() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", null, "scope");
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("callback", config.getCallback());
        assertEquals(SignatureType.Header, config.getSignatureType());
        assertEquals("scope", config.getScope());
    }

    @Test
    public void testConstructorWithNullScope() {
        OAuthConfig config = new OAuthConfig("key", "secret", "callback", SignatureType.QueryString, null);
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals("callback", config.getCallback());
        assertEquals(SignatureType.QueryString, config.getSignatureType());
        assertNull(config.getScope());
    }
}