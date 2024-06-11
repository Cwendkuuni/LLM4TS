package org.scribe.model;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter_validKeyAndValue() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_token", "1234567890");
        Map<String, String> expectedParameters = new HashMap<>();
        expectedParameters.put("oauth_token", "1234567890");
        assertEquals(expectedParameters, request.getOauthParameters());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameter_invalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("not_oauth", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameter_invalidKey2() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("scope", "value");
    }

    @Test
    public void testCheckKey_validKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertEquals("oauth_token", request.checkKey("oauth_token"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_invalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.checkKey("not_oauth");
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
    }
}