package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter_keyStartsWithOAuthPrefix() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_token", "token");

        assertTrue(request.getOauthParameters().containsKey("oauth_token"));
        assertEquals("token", request.getOauthParameters().get("oauth_token"));
    }

    @Test
    public void testAddOAuthParameter_keyIsScope() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("scope", "read write");

        assertTrue(request.getOauthParameters().containsKey("scope"));
        assertEquals("read write", request.getOauthParameters().get("scope"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameter_keyIsNotValid() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("invalid_key", "value");
    }

    @Test
    public void testCheckKey_keyStartsWithOAuthPrefix() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");

        assertEquals("oauth_token", request.checkKey("oauth_token"));
    }

    @Test
    public void testCheckKey_keyIsScope() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");

        assertEquals("scope", request.checkKey("scope"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_keyIsNotValid() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");

        request.checkKey("invalid_key");
    }

    @Test
    public void testGetOauthParameters_noParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");

        assertTrue(request.getOauthParameters().isEmpty());
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");

        assertEquals("@OAuthRequest(GET, https://example.com)", request.toString());
    }
}