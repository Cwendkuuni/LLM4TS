import org.junit.Before;
import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class OAuthRequestTest {

    private OAuthRequest request;

    @Before
    public void setup() {
        request = new OAuthRequest(Verb.GET, "https://example.com");
    }

    @Test
    public void testAddOAuthParameter_withValidKeyAndValue_shouldAddToOAuthParameters() {
        // Arrange
        String key = "oauth_consumer_key";
        String value = "abcd1234";

        // Act
        request.addOAuthParameter(key, value);
        Map<String, String> oauthParameters = request.getOauthParameters();

        // Assert
        assertTrue(oauthParameters.containsKey(key));
        assertEquals(value, oauthParameters.get(key));
    }

    @Test
    public void testAddOAuthParameter_withInvalidKey_shouldThrowIllegalArgumentException() {
        // Arrange
        String key = "invalid_key";
        String value = "abcd1234";

        try {
            // Act
            request.addOAuthParameter(key, value);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'",
                    e.getMessage());
        }
    }

    @Test
    public void testAddOAuthParameter_withValidScopeKey_shouldAddToOAuthParameters() {
        // Arrange
        String key = "scope";
        String value = "read";

        // Act
        request.addOAuthParameter(key, value);
        Map<String, String> oauthParameters = request.getOauthParameters();

        // Assert
        assertTrue(oauthParameters.containsKey(key));
        assertEquals(value, oauthParameters.get(key));
    }

    @Test
    public void testToString_shouldReturnFormattedString() {
        // Arrange
        String expected = "@OAuthRequest(GET, https://example.com)";

        // Act
        String result = request.toString();

        // Assert
        assertEquals(expected, result);
    }

}