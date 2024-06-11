import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_token", "12345");
        
        Map<String, String> oauthParameters = request.getOauthParameters();
        
        assertEquals("12345", oauthParameters.get("oauth_token"));
    }
    
    @Test
    public void testCheckKeyValid() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://example.com");
        
        String key = "oauth_token";
        String result = request.checkKey(key);
        
        assertEquals("oauth_token", result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalid() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://example.com");
        
        String key = "invalid_key";
        request.checkKey(key);
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.DELETE, "https://example.com");
        
        String toString = request.toString();
        
        assertEquals("@OAuthRequest(DELETE, https://example.com)", toString);
    }
}