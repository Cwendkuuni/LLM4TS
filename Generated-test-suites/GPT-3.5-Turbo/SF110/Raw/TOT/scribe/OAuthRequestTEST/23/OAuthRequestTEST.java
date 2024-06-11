import org.junit.*;
import static org.junit.Assert.assertNotNull;

public class OAuthRequestTest {
  
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        
        request.addOAuthParameter("oauth_token", "token123");
        
        assertNotNull(request.getOauthParameters().get("oauth_token"));
    }
    
    @Test
    public void testAddOAuthParameter_WithScope() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        
        request.addOAuthParameter("scope", "read write");
        
        assertNotNull(request.getOauthParameters().get("scope"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameter_InvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        
        request.addOAuthParameter("invalid_key", "value123");
    }
    
    @Test
    public void testCheckKey_StartsWithOAuthPrefix() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        
        String result = request.checkKey("oauth_token");
        
        assertNotNull(result);
    }
    
    @Test
    public void testCheckKey_WithScope() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        
        String result = request.checkKey("scope");
        
        assertNotNull(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_InvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        
        request.checkKey("invalid_key");
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        
        String result = request.toString();
        
        assertNotNull(result);
    }
  
}