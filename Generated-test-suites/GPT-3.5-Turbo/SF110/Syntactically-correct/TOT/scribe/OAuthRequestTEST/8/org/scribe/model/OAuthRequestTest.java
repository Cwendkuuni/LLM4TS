import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        request.addOAuthParameter("oauth_key", "oauth_value");
        Map<String, String> oauthParameters = request.getOauthParameters();
        
        assertEquals(1, oauthParameters.size());
        assertTrue(oauthParameters.containsKey("oauth_key"));
        assertEquals("oauth_value", oauthParameters.get("oauth_key"));
    }

    @Test
    public void testAddOAuthParameterInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        
        try {
            request.addOAuthParameter("invalid_key", "value");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
        
    }
    
    @Test
    public void testCheckKeyValidOAuth() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        String validKey = request.checkKey("oauth_key");
        assertEquals("oauth_key", validKey);
    }
    
    @Test
    public void testCheckKeyValidScope() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        String validKey = request.checkKey("scope");
        assertEquals("scope", validKey);
    }

    @Test
    public void testCheckKeyInvalidOAuth() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        
        try {
            request.checkKey("invalid_key");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
        
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        String expected = "@OAuthRequest(GET, https://www.example.com)";
        assertEquals(expected, request.toString());
    }
}