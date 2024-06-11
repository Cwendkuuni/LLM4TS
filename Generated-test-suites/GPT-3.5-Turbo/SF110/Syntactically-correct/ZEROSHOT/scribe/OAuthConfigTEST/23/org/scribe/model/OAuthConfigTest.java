package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OAuthConfigTest {

    private OAuthConfig oauthConfig;

    @Before
    public void setUp() {
        oauthConfig = new OAuthConfig("apiKey", "apiSecret");
    }

    @Test
    public void testGetApiKey() {
        assertEquals("apiKey", oauthConfig.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        assertEquals("apiSecret", oauthConfig.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        assertEquals("oob", oauthConfig.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        assertEquals(SignatureType.Header, oauthConfig.getSignatureType());
    }

    @Test
    public void testGetScope() {
        assertNull(oauthConfig.getScope());
    }

    @Test
    public void testHasScope() {
        assertFalse(oauthConfig.hasScope());
    }

    @Test
    public void testConstructorWithCallbackAndTypeAndScope() {
        OAuthConfig oauthConfig = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.Query, "scope");
        assertEquals("callback", oauthConfig.getCallback());
        assertEquals(SignatureType.Query, oauthConfig.getSignatureType());
        assertEquals("scope", oauthConfig.getScope());
    }
}