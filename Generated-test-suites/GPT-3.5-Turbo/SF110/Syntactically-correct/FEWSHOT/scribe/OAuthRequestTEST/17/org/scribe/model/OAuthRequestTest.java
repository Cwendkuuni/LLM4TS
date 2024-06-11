package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class OAuthRequestTest {

    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://www.example.com", request.getUrl());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");

        request.addOAuthParameter("oauth_key", "value");
        assertEquals(1, request.getOauthParameters().size());
        assertTrue(request.getOauthParameters().containsKey("oauth_key"));
        assertEquals("value", request.getOauthParameters().get("oauth_key"));

        request.addOAuthParameter("scope", "value");
        assertEquals(2, request.getOauthParameters().size());
        assertTrue(request.getOauthParameters().containsKey("scope"));
        assertEquals("value", request.getOauthParameters().get("scope"));

        assertThrows(IllegalArgumentException.class, () ->
                request.addOAuthParameter("invalid_key", "value"));
    }

    @Test
    public void testCheckKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");

        assertEquals("oauth_key", request.checkKey("oauth_key"));
        assertEquals("scope", request.checkKey("scope"));

        assertThrows(IllegalArgumentException.class, () ->
                request.checkKey("invalid_key"));
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");

        assertEquals("@OAuthRequest(GET, http://www.example.com)", request.toString());
    }
}