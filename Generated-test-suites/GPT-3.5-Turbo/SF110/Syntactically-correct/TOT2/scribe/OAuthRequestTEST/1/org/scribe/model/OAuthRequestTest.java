package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.model.*;

public class OAuthRequestTest {

    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        assertNotNull(request);
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_token", "token123");
        assertEquals("token123", request.getOauthParameters().get("oauth_token"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("invalid_key", "value");
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_token", "token123");
        assertFalse(request.getOauthParameters().isEmpty());
        assertEquals(1, request.getOauthParameters().size());
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        String expected = "@OAuthRequest(GET, https://example.com)";
        assertEquals(expected, request.toString());
    }
}