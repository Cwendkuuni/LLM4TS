package org.scribe.model;

import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class OAuthRequestTest {
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");

        request.addOAuthParameter("oauth_consumer_key", "consumer_key");
        request.addOAuthParameter("oauth_token", "token");
        request.addOAuthParameter("scope", "read");

        Map<String, String> expectedParameters = new HashMap<>();
        expectedParameters.put("oauth_consumer_key", "consumer_key");
        expectedParameters.put("oauth_token", "token");
        expectedParameters.put("scope", "read");

        Map<String, String> actualParameters = request.getOauthParameters();

        assertEquals(expectedParameters, actualParameters);
    }

    @Test
    public void testAddOAuthParameterWithIllegalKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");

        try {
            request.addOAuthParameter("invalid_key", "value");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");

        Map<String, String> expectedParameters = new HashMap<>();
        Map<String, String> actualParameters = request.getOauthParameters();

        assertEquals(expectedParameters, actualParameters);
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");

        String expectedString = "@OAuthRequest(GET, http://example.com)";
        String actualString = request.toString();

        assertEquals(expectedString, actualString);
    }
}