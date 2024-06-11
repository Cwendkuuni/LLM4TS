import static org.junit.Assert.*;
import org.junit.*;

public class OAuthRequestTest {

    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("https://example.com", request.getUrl());
        assertNotNull(request.getOauthParameters());
        assertTrue(request.getOauthParameters().isEmpty());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_token", "12345");
        assertEquals("12345", request.getOauthParameters().get("oauth_token"));
    }

    @Test
    public void testCheckKeyValidKey() {
        String key = "oauth_token";
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        assertEquals(key, request.checkKey(key));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalidKey() {
        String key = "invalid_key";
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.checkKey(key);
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        assertNotNull(request.getOauthParameters());
        assertTrue(request.getOauthParameters().isEmpty());
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        String stringRepresentation = String.format("@OAuthRequest(%s, %s)", request.getVerb(), request.getUrl());
        assertEquals(stringRepresentation, request.toString());
    }
}