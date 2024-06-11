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
    public void testCheckKeyValid() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertEquals("validKey", request.checkKey("validKey"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalid() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.checkKey("invalidKey");
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