package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("testKey", "testValue");

        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals(oauthParameters.get("testKey"), "testValue");
    }

    @Test
    public void testCheckKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        
        // Test for valid key
        String validKey = request.checkKey("oauth_key");
        assertEquals(validKey, "oauth_key");

        // Test for invalid key
        try {
            String invalidKey = request.checkKey("invalid_key");
            fail("Should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "OAuth parameters must either be 'scope' or start with 'oauth_'");
        }
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        Map<String, String> oauthParameters = request.getOauthParameters();

        assertNotNull(oauthParameters);
        assertTrue(oauthParameters.isEmpty());
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        String expectedString = "@OAuthRequest(GET, https://example.com)";

        assertEquals(request.toString(), expectedString);
    }
}