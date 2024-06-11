import org.junit.*;
import static org.junit.Assert.*;

public class OAuthRequestTest {
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "CONSUMER_KEY");
        
        assertEquals("CONSUMER_KEY", request.getOauthParameters().get("oauth_consumer_key"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameter_invalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("invalid_key", "VALUE");
    }
    
    @Test
    public void testCheckKey_validKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        String key = request.checkKey("oauth_token");
        
        assertEquals("oauth_token", key);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_invalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.checkKey("invalid_key");
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        String expectedString = "@OAuthRequest(GET, http://example.com)";
        
        assertEquals(expectedString, request.toString());
    }
}