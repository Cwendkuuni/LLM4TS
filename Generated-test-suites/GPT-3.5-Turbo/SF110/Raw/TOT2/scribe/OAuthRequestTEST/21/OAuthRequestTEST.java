import static org.junit.Assert.*;
import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;
import java.util.Map;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_token", "token123");
        
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertTrue(oauthParameters.containsKey("oauth_token"));
        assertEquals("token123", oauthParameters.get("oauth_token"));
    }
    
    @Test
    public void testCheckKeyValidPrefix() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        
        assertEquals("oauth_token", request.checkKey("oauth_token"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        
        request.checkKey("not_oauth_token");
    }
    
    @Test
    public void testCheckKeyValidScope() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        
        assertEquals("scope", request.checkKey("scope"));
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        
        assertEquals("@OAuthRequest(GET, https://example.com)", request.toString());
    }
}