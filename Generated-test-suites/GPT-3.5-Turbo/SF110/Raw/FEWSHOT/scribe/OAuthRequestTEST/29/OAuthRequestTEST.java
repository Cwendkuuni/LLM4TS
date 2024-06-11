package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class OAuthRequestTest {

    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("https://www.example.com", request.getUrl());
        assertNotNull(request.getOauthParameters());
        assertTrue(request.getOauthParameters().isEmpty());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        request.addOAuthParameter("oauth_token", "abc123");
        request.addOAuthParameter("scope", "read");
        assertEquals(2, request.getOauthParameters().size());
        assertTrue(request.getOauthParameters().containsKey("oauth_token"));
        assertTrue(request.getOauthParameters().containsKey("scope"));
        assertEquals("abc123", request.getOauthParameters().get("oauth_token"));
        assertEquals("read", request.getOauthParameters().get("scope"));
    }

    @Test
    public void testCheckKey_validKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        assertEquals("oauth_token", request.checkKey("oauth_token"));
        assertEquals("scope", request.checkKey("scope"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_invalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        request.checkKey("invalid_key");
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        assertEquals("@OAuthRequest(GET, https://www.example.com)", request.toString());
    }
}