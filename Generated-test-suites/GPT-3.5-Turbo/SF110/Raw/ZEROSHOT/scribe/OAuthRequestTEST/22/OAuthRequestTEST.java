package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        request.addOAuthParameter("key", "value");

        assertEquals("value", request.getOauthParameters().get("key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        request.addOAuthParameter("invalid_key", "value");
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        request.addOAuthParameter("key", "value");

        assertEquals(1, request.getOauthParameters().size());
        assertEquals("value", request.getOauthParameters().get("key"));
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");

        assertEquals("@OAuthRequest(GET, https://www.example.com)", request.toString());
    }
}