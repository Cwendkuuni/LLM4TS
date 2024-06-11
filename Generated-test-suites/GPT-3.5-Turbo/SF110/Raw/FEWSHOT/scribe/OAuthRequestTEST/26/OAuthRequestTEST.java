
import static org.junit.Assert.*;
import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;
import java.util.HashMap;
import java.util.Map;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");
        request.addOAuthParameter("oauth_key", "value");
        Map<String, String> parameters = request.getOauthParameters();

        // Test case when adding a valid OAuth parameter
        assertEquals("value", parameters.get("oauth_key"));

        // Test case when adding a parameter that starts with "oauth_" prefix
        request.addOAuthParameter("invalid_key", "value");
        assertEquals("value", parameters.get("invalid_key"));

        // Test case when adding a parameter that starts with "oauth_" prefix and "scope" key
        request.addOAuthParameter("scope", "value");
        assertEquals("value", parameters.get("scope"));

        // Test case when adding a parameter that neither starts with "oauth_" prefix nor "scope" key
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            request.addOAuthParameter("invalid_key_two", "value");
        });
        String expectedErrorMessage = "OAuth parameters must either be 'scope' or start with 'oauth_'";
        String actualErrorMessage = exception.getMessage();
        assertTrue(actualErrorMessage.contains(expectedErrorMessage));
    }

    @Test
    public void testCheckKey() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");

        // Test case when key starts with "oauth_" prefix
        String validKey = "oauth_key";
        String actualKey = request.checkKey(validKey);
        assertEquals(validKey, actualKey);

        // Test case when key is "scope"
        String scopeKey = "scope";
        actualKey = request.checkKey(scopeKey);
        assertEquals(scopeKey, actualKey);

        // Test case when key does not start with "oauth_" prefix or is "scope"
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            request.checkKey("invalid_key");
        });
        String expectedErrorMessage = "OAuth parameters must either be 'scope' or start with 'oauth_'";
        String actualErrorMessage = exception.getMessage();
        assertTrue(actualErrorMessage.contains(expectedErrorMessage));
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");
        Map<String, String> parameters = request.getOauthParameters();

        // Test case when retrieving empty oauthParameters map
        assertNotNull(parameters);
        assertTrue(parameters.isEmpty());

        // Test case after adding an OAuth parameter
        request.addOAuthParameter("oauth_key", "value");
        parameters = request.getOauthParameters();
        assertNotNull(parameters);
        assertFalse(parameters.isEmpty());
        assertEquals("value", parameters.get("oauth_key"));
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");

        // Test case for Verb.POST and url "http://example.com"
        String expectedString = "@OAuthRequest(POST, http://example.com)";
        String actualString = request.toString();
        assertEquals(expectedString, actualString);

        // Test case for Verb.GET and url "https://example.com"
        request = new OAuthRequest(Verb.GET, "https://example.com");
        expectedString = "@OAuthRequest(GET, https://example.com)";
        actualString = request.toString();
        assertEquals(expectedString, actualString);
    }
}
