
package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Map;

public class OAuthRequestTest {

    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("https://example.com", request.getUrl());

        Map<String, String> oauthParameters = request.getOauthParameters();
        assertNotNull(oauthParameters);
        assertTrue(oauthParameters.isEmpty());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_token", "12345");

        Map<String, String> oauthParameters = request.getOauthParameters();
        assertFalse(oauthParameters.isEmpty());
        assertEquals(1, oauthParameters.size());
        assertEquals("12345", oauthParameters.get("oauth_token"));

        request.addOAuthParameter("scope", "read");
        assertTrue(oauthParameters.containsKey("scope"));
        assertEquals("read", oauthParameters.get("scope"));

        try {
            request.addOAuthParameter("invalid_parameter", "value");
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }

    @Test
    public void testCheckKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");

        assertEquals("oauth_token", request.checkKey("oauth_token"));
        assertEquals("scope", request.checkKey("scope"));

        try {
            request.checkKey("invalid_parameter");
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com/api");
        assertEquals("@OAuthRequest(GET, https://example.com/api)", request.toString());

        request = new OAuthRequest(Verb.POST, "https://example.com/api");
        assertEquals("@OAuthRequest(POST, https://example.com/api)", request.toString());

        request = new OAuthRequest(Verb.DELETE, "https://example.com/api");
        assertEquals("@OAuthRequest(DELETE, https://example.com/api)", request.toString());
    }
}
