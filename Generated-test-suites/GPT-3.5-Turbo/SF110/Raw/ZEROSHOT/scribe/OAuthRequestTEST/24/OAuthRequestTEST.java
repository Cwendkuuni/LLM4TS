package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter_ValidKey_ValidValue() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://www.example.com");
        oauthRequest.addOAuthParameter("oauth_token", "token123");

        Map<String, String> expectedParameters = new HashMap<String, String>();
        expectedParameters.put("oauth_token", "token123");

        assertEquals(expectedParameters, oauthRequest.getOauthParameters());
    }

    @Test
    public void testAddOAuthParameter_InvalidKey() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://www.example.com");

        try {
            oauthRequest.addOAuthParameter("invalid_key", "value123");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "OAuth parameters must either be 'scope' or start with 'oauth_'");
        }
    }

    @Test
    public void testAddOAuthParameter_ValidKey_Scope() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://www.example.com");
        oauthRequest.addOAuthParameter("scope", "read");

        Map<String, String> expectedParameters = new HashMap<String, String>();
        expectedParameters.put("scope", "read");

        assertEquals(expectedParameters, oauthRequest.getOauthParameters());
    }

    @Test
    public void testToString() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://www.example.com");
        String expectedToString = "@OAuthRequest(GET, http://www.example.com)";

        assertEquals(expectedToString, oauthRequest.toString());
    }
}