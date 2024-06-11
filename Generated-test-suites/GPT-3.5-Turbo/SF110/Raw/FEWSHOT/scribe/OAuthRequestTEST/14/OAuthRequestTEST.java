package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class OAuthRequestTest {

    private OAuthRequest request;

    @Before
    public void setup() {
        request = new OAuthRequest(Verb.GET, "http://example.com");
    }

    @Test
    public void testAddOAuthParameter() {
        Map<String, String> expectedParameters = new HashMap<>();
        assertEquals(expectedParameters, request.getOauthParameters());

        String key = "oauth_key";
        String value = "oauth_value";
        request.addOAuthParameter(key, value);
        expectedParameters.put(key, value);
        assertEquals(expectedParameters, request.getOauthParameters());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKey() {
        request.addOAuthParameter("invalid_key", "oauth_value");
    }

    @Test
    public void testCheckKeyWithValidKey() {
        String validKey = "oauth_key";
        assertEquals(validKey, request.checkKey(validKey));

        String scopeKey = "scope";
        assertEquals(scopeKey, request.checkKey(scopeKey));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyWithInvalidKey() {
        request.checkKey("invalid_key");
    }

    @Test
    public void testGetOauthParameters() {
        Map<String, String> expectedParameters = new HashMap<>();
        assertEquals(expectedParameters, request.getOauthParameters());

        String key = "oauth_key";
        String value = "oauth_value";
        request.addOAuthParameter(key, value);
        expectedParameters.put(key, value);
        assertEquals(expectedParameters, request.getOauthParameters());
    }

    @Test
    public void testToString() {
        String verb = "GET";
        String url = "http://example.com";
        String expectedToString = String.format("@OAuthRequest(%s, %s)", verb, url);
        request = new OAuthRequest(Verb.GET, url);

        assertEquals(expectedToString, request.toString());
    }
}