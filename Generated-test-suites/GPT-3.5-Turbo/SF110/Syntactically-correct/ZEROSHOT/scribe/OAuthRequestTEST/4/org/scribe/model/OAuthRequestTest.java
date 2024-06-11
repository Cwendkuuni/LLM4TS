package org.scribe.model;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthRequestTest {

    private OAuthRequest oauthRequest;

    @Before
    public void setUp() {
        oauthRequest = new OAuthRequest(Verb.GET, "https://example.com");
    }

    @Test
    public void testAddOAuthParameter() {
        oauthRequest.addOAuthParameter("oauth_token", "12345");
        assertEquals("12345", oauthRequest.getOauthParameters().get("oauth_token"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKey() {
        oauthRequest.addOAuthParameter("invalid_key", "12345");
    }

    @Test
    public void testGetOauthParameters() {
        assertTrue(oauthRequest.getOauthParameters().isEmpty());
        oauthRequest.addOAuthParameter("oauth_token", "12345");
        assertFalse(oauthRequest.getOauthParameters().isEmpty());
    }

    @Test
    public void testToString() {
        String expected = "@OAuthRequest(GET, https://example.com)";
        assertEquals(expected, oauthRequest.toString());
    }
}