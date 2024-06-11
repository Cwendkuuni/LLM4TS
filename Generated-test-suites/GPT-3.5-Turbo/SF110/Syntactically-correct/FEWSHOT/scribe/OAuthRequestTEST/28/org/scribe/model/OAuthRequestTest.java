package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class OAuthRequestTest {
    private OAuthRequest request;

    @Before
    public void setUp() {
        request = new OAuthRequest(Verb.GET, "http://example.com");
    }

    @Test
    public void testAddOAuthParameter() {
        request.addOAuthParameter("oauth_token", "12345");
        request.addOAuthParameter("scope", "read");

        Map<String, String> oauthParameters = request.getOauthParameters();
        assertNotNull(oauthParameters);
        assertEquals("12345", oauthParameters.get("oauth_token"));
        assertEquals("read", oauthParameters.get("scope"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterInvalidKey() {
        request.addOAuthParameter("invalid_key", "value");
    }

    @Test
    public void testCheckKeyOAuthParameter() {
        String key = "oauth_token";
        String checkedKey = request.checkKey(key);
        assertEquals(key, checkedKey);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalidKey() {
        request.checkKey("invalid_key");
    }

    @Test
    public void testCheckKeyScopeParameter() {
        String key = "scope";
        String checkedKey = request.checkKey(key);
        assertEquals(key, checkedKey);
    }

    @Test
    public void testGetOauthParameters() {
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertNotNull(oauthParameters);
        assertEquals(new HashMap<String, String>(), oauthParameters);
    }

    @Test
    public void testToString() {
        String expectedString = "@OAuthRequest(GET, http://example.com)";
        String actualString = request.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testEquality() {
        OAuthRequest request2 = new OAuthRequest(Verb.GET, "http://example.com");
        OAuthRequest request3 = new OAuthRequest(Verb.POST, "http://example.com");
        assertEquals(request, request);
        assertEquals(request, request2);
        assertNotEquals(request, request3);
        assertNotEquals(request, null);
        assertNotEquals(request, new Object());
    }
}