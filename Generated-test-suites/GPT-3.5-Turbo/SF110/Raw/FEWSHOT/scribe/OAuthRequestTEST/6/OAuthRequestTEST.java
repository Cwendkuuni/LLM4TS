
package org.scribe.model;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        request.addOAuthParameter("oauth_token", "12345");
        request.addOAuthParameter("oauth_consumer_key", "54321");

        Map<String, String> expectedParameters = new HashMap<>();
        expectedParameters.put("oauth_token", "12345");
        expectedParameters.put("oauth_consumer_key", "54321");

        assertEquals(expectedParameters, request.getOauthParameters());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        request.addOAuthParameter("invalid_key", "value");
    }

    @Test
    public void testCheckKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");

        assertEquals("oauth_token", request.checkKey("oauth_token"));
        assertEquals("oauth_consumer_key", request.checkKey("oauth_consumer_key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        request.checkKey("invalid_key");
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        request.addOAuthParameter("oauth_token", "12345");
        request.addOAuthParameter("oauth_consumer_key", "54321");

        Map<String, String> expectedParameters = new HashMap<>();
        expectedParameters.put("oauth_token", "12345");
        expectedParameters.put("oauth_consumer_key", "54321");

        assertEquals(expectedParameters, request.getOauthParameters());
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        assertEquals("@OAuthRequest(GET, http://www.example.com)", request.toString());
    }
}
