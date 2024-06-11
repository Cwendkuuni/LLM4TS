import static org.junit.Assert.*;
import org.junit.Test;

public class OAuthRequestTest {

    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("https://example.com", request.getUrl());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://example.com");
        request.addOAuthParameter("oauth_token", "abcdef12345");
        assertEquals("abcdef12345", request.getOauthParameters().get("oauth_token"));
    }

    @Test
    public void testCheckKey_validKey() {
        OAuthRequest request = new OAuthRequest(Verb.PUT, "https://example.com");
        assertEquals("scope", request.checkKey("scope"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_invalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.DELETE, "https://example.com");
        request.checkKey("invalid_key");
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.PATCH, "https://example.com");
        assertEquals("@OAuthRequest(PATCH, https://example.com)", request.toString());
    }

}