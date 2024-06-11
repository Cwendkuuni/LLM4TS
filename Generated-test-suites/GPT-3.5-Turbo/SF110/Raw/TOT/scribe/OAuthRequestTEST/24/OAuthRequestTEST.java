import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class OAuthRequestTest {

    @Test
    public void testConstructor() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");

        assertEquals(Verb.GET, oauthRequest.getVerb());
        assertEquals("http://example.com", oauthRequest.getUrl());
        assertNotNull(oauthRequest.getOauthParameters());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");

        oauthRequest.addOAuthParameter("oauth_token", "token1");
        oauthRequest.addOAuthParameter("oauth_token_secret", "secret1");

        Map<String, String> expectedParameters = new HashMap<>();
        expectedParameters.put("oauth_token", "token1");
        expectedParameters.put("oauth_token_secret", "secret1");

        assertEquals(expectedParameters, oauthRequest.getOauthParameters());

        oauthRequest.addOAuthParameter("scope", "email");

        assertEquals("email", oauthRequest.getOauthParameters().get("scope"));

        try {
            oauthRequest.addOAuthParameter("invalid_key", "value");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }

    @Test
    public void testCheckKey() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");

        assertEquals("oauth_token", oauthRequest.checkKey("oauth_token"));
        assertEquals("scope", oauthRequest.checkKey("scope"));

        try {
            oauthRequest.checkKey("invalid_key");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }

    @Test
    public void testToString() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");

        assertEquals("@OAuthRequest(GET, http://example.com)", oauthRequest.toString());
    }
}