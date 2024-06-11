package org.scribe.model;
import static org.junit.Assert.*;
import org.junit.Test;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_token", "12345");
        assertEquals(1, request.getOauthParameters().size());
    }

    @Test
    public void testCheckKeyValid() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        String key = "oauth_consumer_key";
        assertEquals(key, request.checkKey(key));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalid() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        String key = "invalid_key";
        request.checkKey(key);
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertNotNull(request.getOauthParameters());
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
    }
}