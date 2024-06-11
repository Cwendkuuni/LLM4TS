import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.model.*;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_token", "12345");
        assertEquals("12345", request.getOauthParameters().get("oauth_token"));
    }

    @Test
    public void testAddOAuthParameter_Exception() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        try {
            request.addOAuthParameter("invalid_key", "value");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        Map<String, String> parameters = request.getOauthParameters();
        assertNotNull(parameters);
        assertTrue(parameters.isEmpty());
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        assertEquals("@OAuthRequest(GET, https://example.com)", request.toString());
    }
}