package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class OAuthRequestTest {

    private OAuthRequest request;

    @Before
    public void setUp() {
        request = new OAuthRequest(Verb.GET, "https://example.com");
    }

    @Test
    public void testAddOAuthParameter() {
        request.addOAuthParameter("key1", "value1");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals("value1", oauthParameters.get("key1"));

        request.addOAuthParameter("key2", "value2");
        oauthParameters = request.getOauthParameters();
        assertEquals("value2", oauthParameters.get("key2"));
    }

    @Test
    public void testCheckKey() {
        assertTrue(request.checkKey("oauth_key"));
        assertTrue(request.checkKey("scope"));

        boolean exceptionThrown = false;
        try {
            request.checkKey("key");
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testGetOauthParameters() {
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertTrue(oauthParameters instanceof HashMap);
        assertEquals(0, oauthParameters.size());

        request.addOAuthParameter("key1", "value1");
        oauthParameters = request.getOauthParameters();
        assertEquals(1, oauthParameters.size());
        assertEquals("value1", oauthParameters.get("key1"));
    }

    @Test
    public void testToString() {
        String toString = request.toString();
        assertEquals("@OAuthRequest(GET, https://example.com)", toString);
    }
}