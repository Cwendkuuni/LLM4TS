import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthRequestTest {

    // Test constructor
    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("https://example.com", request.getUrl());
        assertEquals(0, request.getOauthParameters().size());
    }

    // Test addOAuthParameter method
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_key", "oauth_value");

        assertEquals(1, request.getOauthParameters().size());
        assertEquals("oauth_value", request.getOauthParameters().get("oauth_key"));
    }

    // Test checkKey method with valid key
    @Test
    public void testCheckKeyValid() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        String validKey = "oauth_key";

        assertEquals(validKey, request.checkKey(validKey));
    }

    // Test checkKey method with invalid key
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalid() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        String invalidKey = "invalid_key";

        request.checkKey(invalidKey);
    }

    // Test getOauthParameters method
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_key", "oauth_value");

        assertEquals(1, request.getOauthParameters().size());
        assertEquals("oauth_value", request.getOauthParameters().get("oauth_key"));
    }

    // Test toString method
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");

        assertEquals("@OAuthRequest(GET, https://example.com)", request.toString());
    }
}


// Other edge cases and exception handling scenarios to be tested here

