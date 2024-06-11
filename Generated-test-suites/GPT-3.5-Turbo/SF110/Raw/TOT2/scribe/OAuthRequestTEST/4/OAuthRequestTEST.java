import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Map;

public class OAuthRequestTest {

    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        assertEquals("GET", request.getVerb().toString());
        assertEquals("http://www.example.com", request.getUrl());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://www.example.com");
        request.addOAuthParameter("testKey", "testValue");
        Map<String, String> oauthParams = request.getOauthParameters();
        assertTrue(oauthParams.containsKey("testKey"));
        assertEquals("testValue", oauthParams.get("testKey"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.PUT, "http://www.example.com");
        request.addOAuthParameter("invalidKey", "testValue");
    }

    @Test
    public void testGetOauthParametersEmpty() {
        OAuthRequest request = new OAuthRequest(Verb.DELETE, "http://www.example.com");
        Map<String, String> oauthParams = request.getOauthParameters();
        assertTrue(oauthParams.isEmpty());
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.PATCH, "http://www.example.com");
        assertEquals("@OAuthRequest(PATCH, http://www.example.com)", request.toString());
    }
}