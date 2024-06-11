import org.junit.Test;
import org.scribe.model.Verb;
import static org.junit.Assert.*;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_consumer_key", "xyz123");

        assertTrue(request.getOauthParameters().containsKey("oauth_consumer_key"));
        assertEquals("xyz123", request.getOauthParameters().get("oauth_consumer_key"));
    }

    @Test
    public void testCheckKey_ValidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        String key = "oauth_consumer_key";

        assertEquals(key, request.checkKey(key));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_InvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.checkKey("invalid_key");
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_consumer_key", "xyz123");
        request.addOAuthParameter("oauth_token", "abc456");

        assertTrue(request.getOauthParameters().containsKey("oauth_consumer_key"));
        assertTrue(request.getOauthParameters().containsKey("oauth_token"));
        assertEquals("xyz123", request.getOauthParameters().get("oauth_consumer_key"));
        assertEquals("abc456", request.getOauthParameters().get("oauth_token"));
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");

        assertEquals("@OAuthRequest(GET, https://example.com)", request.toString());
    }
}