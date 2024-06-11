import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");

        // Test adding a valid OAuth parameter
        request.addOAuthParameter("oauth_token", "accessToken");
        assertEquals("accessToken", request.getOauthParameters().get("oauth_token"));

        // Test adding an invalid OAuth parameter
        try {
            request.addOAuthParameter("invalid_key", "value");
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }

    @Test
    public void testCheckKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");

        // Test checking a valid OAuth key
        String validKey = request.checkKey("oauth_token");
        assertEquals("oauth_token", validKey);

        // Test checking a non-OAuth key
        String nonOAuthKey = request.checkKey("non_oauth");
        assertEquals("non_oauth", nonOAuthKey);

        // Test checking an invalid OAuth key
        try {
            request.checkKey("invalid_key");
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");

        // Test getting OAuth parameters when none are added
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertNotNull(oauthParameters);
        assertTrue(oauthParameters.isEmpty());

        // Test getting OAuth parameters after adding some
        request.addOAuthParameter("oauth_token", "accessToken");
        oauthParameters = request.getOauthParameters();
        assertNotNull(oauthParameters);
        assertEquals(1, oauthParameters.size());
        assertEquals("accessToken", oauthParameters.get("oauth_token"));
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");

        // Test getting string representation for OAuthRequest
        assertEquals("@OAuthRequest(POST, http://example.com)", request.toString());
    }
}