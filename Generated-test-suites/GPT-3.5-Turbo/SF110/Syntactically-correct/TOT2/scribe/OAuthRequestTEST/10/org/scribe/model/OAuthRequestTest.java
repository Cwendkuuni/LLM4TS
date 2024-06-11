package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.model.*;

public class OAuthRequestTest {

    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        assertNotNull(request);
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("https://www.example.com", request.getUrl());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        request.addOAuthParameter("test_key", "test_value");
        assertTrue(request.getOauthParameters().containsKey("test_key"));
        assertEquals("test_value", request.getOauthParameters().get("test_key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        request.addOAuthParameter("invalid_key", "test_value");
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        request.addOAuthParameter("test_key", "test_value");
        assertEquals(1, request.getOauthParameters().size());
        assertTrue(request.getOauthParameters().containsKey("test_key"));
        assertEquals("test_value", request.getOauthParameters().get("test_key"));
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        assertEquals("@OAuthRequest(GET, https://www.example.com)", request.toString());
    }
}