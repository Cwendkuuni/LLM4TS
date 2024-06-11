import org.junit.*;
import static org.junit.Assert.*;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        request.addOAuthParameter("oauth_token", "123456");
        
        assertEquals("123456", request.getOauthParameters().get("oauth_token"));
    }
    
    @Test
    public void testCheckKey_validKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        
        String key = request.checkKey("oauth_token");
        
        assertEquals("oauth_token", key);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_invalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        
        request.checkKey("invalid_key");
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        request.addOAuthParameter("oauth_token", "123456");
        request.addOAuthParameter("scope", "read");
        
        assertEquals("123456", request.getOauthParameters().get("oauth_token"));
        assertEquals("read", request.getOauthParameters().get("scope"));
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        
        String expected = "@OAuthRequest(GET, https://www.example.com)";
        assertEquals(expected, request.toString());
    }
}