import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthRequestTest {

    // Test case for addOAuthParameter() method
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_token", "123456789");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals("123456789", oauthParameters.get("oauth_token"));
    }

    // Test case for checkKey() method - valid key
    @Test
    public void testCheckKeyValid() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        String key = request.checkKey("oauth_token");
        assertEquals("oauth_token", key);
    }

    // Test case for checkKey() method - invalid key
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalid() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.checkKey("invalid_key");
    }

    // Test case for getOauthParameters() method
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertNotNull(oauthParameters);
    }

    // Test case for toString() method
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        String result = request.toString();
        assertEquals("@OAuthRequest(GET, https://example.com)", result);
    }

}