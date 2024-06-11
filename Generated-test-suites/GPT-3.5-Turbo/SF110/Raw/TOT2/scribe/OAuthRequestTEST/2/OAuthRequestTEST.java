import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Map;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("testKey", "testValue");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals("testValue", oauthParameters.get("testKey"));
    }

    @Test
    public void testCheckKey_validKey() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://example.com");
        String validKey = "oauth_valid";
        String result = request.checkKey(validKey);
        assertEquals(validKey, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_invalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.PUT, "https://example.com");
        String invalidKey = "invalid_key";
        request.checkKey(invalidKey);
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.DELETE, "https://example.com");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertNotNull(oauthParameters);
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.PATCH, "https://example.com");
        String expectedString = "@OAuthRequest(PATCH, https://example.com)";
        assertEquals(expectedString, request.toString());
    }
}