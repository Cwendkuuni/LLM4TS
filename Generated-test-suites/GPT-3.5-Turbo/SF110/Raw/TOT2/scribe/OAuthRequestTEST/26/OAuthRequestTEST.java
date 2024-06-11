import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

import java.util.Map;

import static org.junit.Assert.*;

public class OAuthRequestTest {

    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertNotNull(request);
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");
        request.addOAuthParameter("testKey", "testValue");
        
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertTrue(oauthParameters.containsKey("testKey"));
        assertEquals("testValue", oauthParameters.get("testKey"));
    }

    @Test
    public void testCheckKeyValidKey() {
        OAuthRequest request = new OAuthRequest(Verb.PUT, "http://example.com");
        assertEquals("oauth_token", request.checkKey("oauth_token"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.DELETE, "http://example.com");
        request.checkKey("invalidKey");
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.PATCH, "http://example.com");
        assertEquals("@OAuthRequest(PATCH, http://example.com)", request.toString());
    }
}