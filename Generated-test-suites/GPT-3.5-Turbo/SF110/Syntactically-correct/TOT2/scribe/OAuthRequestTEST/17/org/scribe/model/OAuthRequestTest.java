package org.scribe.model;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_token", "token123");
        Map<String, String> oauthParams = request.getOauthParameters();
        assertTrue(oauthParams.containsKey("oauth_token"));
        assertEquals("token123", oauthParams.get("oauth_token"));
    }

    @Test
    public void testCheckKeyValidInput() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        String result = request.checkKey("oauth_token");
        assertEquals("oauth_token", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalidInput() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.checkKey("invalid_key");
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        Map<String, String> oauthParams = request.getOauthParameters();
        assertNotNull(oauthParams);
        assertEquals(0, oauthParams.size());
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        assertEquals("@OAuthRequest(GET, https://example.com)", request.toString());
    }
}