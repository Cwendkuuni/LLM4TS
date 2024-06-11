import org.junit.Test;
import org.scribe.model.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        // Arrange
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        String key = "oauth_key";
        String value = "oauth_value";

        // Act
        request.addOAuthParameter(key, value);

        // Assert
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertTrue(oauthParameters.containsKey(key));
        assertEquals(value, oauthParameters.get(key));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterInvalidKey() {
        // Arrange
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        String key = "invalid_key";
        String value = "oauth_value";

        // Act
        request.addOAuthParameter(key, value);
    }

    @Test
    public void testCheckKeyValidKey() {
        // Arrange
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        String key = "oauth_key";

        // Act
        String result = request.checkKey(key);

        // Assert
        assertEquals(key, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalidKey() {
        // Arrange
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        String key = "invalid_key";

        // Act
        request.checkKey(key);
    }

    @Test
    public void testGetOauthParameters() {
        // Arrange
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");

        // Act
        Map<String, String> oauthParameters = request.getOauthParameters();

        // Assert
        assertNotNull(oauthParameters);
        assertTrue(oauthParameters.isEmpty());
    }

    @Test
    public void testToString() {
        // Arrange
        Verb verb = Verb.GET;
        String url = "http://example.com";
        OAuthRequest request = new OAuthRequest(verb, url);

        // Act
        String result = request.toString();

        // Assert
        assertEquals("@OAuthRequest(GET, http://example.com)", result);
    }
}