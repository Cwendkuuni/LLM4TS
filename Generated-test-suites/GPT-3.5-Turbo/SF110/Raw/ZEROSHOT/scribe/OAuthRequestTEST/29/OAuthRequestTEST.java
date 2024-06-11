import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://www.example.com");
        oauthRequest.addOAuthParameter("testKey", "testValue");

        String value = oauthRequest.getOauthParameters().get("testKey");

        assertEquals("testValue", value);
    }

    @Test
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://www.example.com");

        try {
            oauthRequest.addOAuthParameter("invalidKey", "testValue");
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }

    @Test
    public void testCheckKeyWithValidKey() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://www.example.com");

        String key = oauthRequest.checkKey("oauth_testKey");

        assertEquals("oauth_testKey", key);
    }

    @Test
    public void testCheckKeyWithInvalidKey() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://www.example.com");

        try {
            oauthRequest.checkKey("invalidKey");
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://www.example.com");
        oauthRequest.addOAuthParameter("testKey", "testValue");

        Map<String, String> parameters = oauthRequest.getOauthParameters();

        assertEquals(1, parameters.size());
        assertEquals("testValue", parameters.get("testKey"));
    }

    @Test
    public void testToString() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://www.example.com");

        String result = oauthRequest.toString();

        assertEquals("@OAuthRequest(GET, http://www.example.com)", result);
    }
}