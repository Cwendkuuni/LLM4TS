package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");

        // Test adding a valid oauth parameter
        request.addOAuthParameter("oauth_token", "12345");
        Map<String, String> expectedParameters = new HashMap<>();
        expectedParameters.put("oauth_token", "12345");
        assertEquals(expectedParameters, request.getOauthParameters());

        // Test adding a scope parameter
        request.addOAuthParameter("scope", "read write");
        expectedParameters.put("scope", "read write");
        assertEquals(expectedParameters, request.getOauthParameters());

        // Test adding an invalid oauth parameter
        try {
            request.addOAuthParameter("invalid_key", "value");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }

    @Test
    public void testCheckKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");

        // Test a valid oauth parameter
        assertEquals("oauth_token", request.checkKey("oauth_token"));

        // Test a valid scope parameter
        assertEquals("scope", request.checkKey("scope"));

        // Test an invalid parameter
        try {
            request.checkKey("invalid_key");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        assertEquals("@OAuthRequest(GET, https://www.example.com)", request.toString());
    }
}