import org.junit.Test;
import java.util.Map;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_key", "oauth_value");

        // Test if the parameter is added correctly
        assertEquals("oauth_value", request.getOauthParameters().get("oauth_key"));
    }

    @Test
    public void testAddOAuthParameterInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");

        // Test if an IllegalArgumentException is thrown for an invalid key
        assertThrows(IllegalArgumentException.class, () -> {
            request.addOAuthParameter("invalid_key", "oauth_value");
        });
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");

        // Test if the oauthParameters map is not null
        assertNotNull(request.getOauthParameters());
        // Test if the oauthParameters map is empty initially
        assertTrue(request.getOauthParameters().isEmpty());
        // Test if the oauthParameters map is correctly updated after adding a parameter
        request.addOAuthParameter("oauth_key", "oauth_value");
        assertFalse(request.getOauthParameters().isEmpty());
        assertEquals(1, request.getOauthParameters().size());
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");

        // Test if the toString method returns the correct format
        assertEquals("@OAuthRequest(GET, https://example.com)", request.toString());
    }
}