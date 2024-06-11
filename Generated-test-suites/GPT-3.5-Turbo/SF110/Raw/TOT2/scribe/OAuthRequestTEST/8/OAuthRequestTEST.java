import static org.junit.Assert.*;
import org.junit.Test;
import org.scribe.model.*;

public class OAuthRequestTest {

    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "example.com");
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("example.com", request.getUrl());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "example.com");
        request.addOAuthParameter("oauth_consumer_key", "123456");
        assertEquals("123456", request.getOauthParameters().get("oauth_consumer_key"));
    }

    @Test
    public void testCheckKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "example.com");

        // Testing valid key
        assertEquals("valid_key", request.checkKey("valid_key"));

        // Testing key starting with "oauth_"
        assertEquals("oauth_consumer_key", request.checkKey("oauth_consumer_key"));

        // Testing invalid key
        try {
            request.checkKey("invalid_key");
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "example.com");
        assertEquals("@OAuthRequest(GET, example.com)", request.toString());
    }
}