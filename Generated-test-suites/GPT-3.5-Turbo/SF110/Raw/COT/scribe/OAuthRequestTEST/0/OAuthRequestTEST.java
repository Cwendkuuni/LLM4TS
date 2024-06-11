import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import java.util.concurrent.*;

public class OAuthRequestTest {

    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
    }
    
    @Test
    public void testAddOauthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_key", "value");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertTrue(oauthParameters.containsKey("oauth_key"));
        assertEquals("value", oauthParameters.get("oauth_key"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddOauthParameterInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("key", "value");
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_key", "value");
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals(1, oauthParameters.size());
        assertEquals("value", oauthParameters.get("oauth_key"));
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        String expected = "@OAuthRequest(GET, http://example.com)";
        assertEquals(expected, request.toString());
    }
}