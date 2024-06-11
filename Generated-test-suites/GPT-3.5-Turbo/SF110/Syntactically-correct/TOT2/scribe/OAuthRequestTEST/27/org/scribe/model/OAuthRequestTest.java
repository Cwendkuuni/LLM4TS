package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("testKey", "testValue");
        assertEquals("testValue", request.getOauthParameters().get("testKey"));
    }

    @Test
    public void testCheckKeyValidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertEquals("oauth_test", request.checkKey("oauth_test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.checkKey("invalidKey");
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("key1", "value1");
        assertEquals("value1", request.getOauthParameters().get("key1"));
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
    }
}