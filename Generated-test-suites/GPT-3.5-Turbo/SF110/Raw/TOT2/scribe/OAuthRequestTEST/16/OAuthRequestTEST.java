import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        request.addOAuthParameter("oauth_consumer_key", "12345");
        Map<String, String> oauthParams = request.getOauthParameters();
        assertEquals(1, oauthParams.size());
        assertEquals("12345", oauthParams.get("oauth_consumer_key"));
    }

    @Test
    public void testCheckKeyValid() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        String validKey = "oauth_consumer_key";
        assertEquals(validKey, request.checkKey(validKey));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalid() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        String invalidKey = "invalid_key";
        request.checkKey(invalidKey);
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        Map<String, String> oauthParams = request.getOauthParameters();
        assertTrue(oauthParams.isEmpty());
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        assertEquals("@OAuthRequest(GET, http://www.example.com)", request.toString());
    }
}