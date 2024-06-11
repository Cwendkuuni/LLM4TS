import org.junit.Test;
import java.util.Map;
import java.util.HashMap;

import static org.junit.Assert.*;

public class OAuthRequestTest {
    
    @Test
    public void testAddOAuthParameter_typical() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        
        oauthRequest.addOAuthParameter("oauth_consumer_key", "consumer_key");
        
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertTrue(oauthParameters.containsKey("oauth_consumer_key"));
        assertEquals("consumer_key", oauthParameters.get("oauth_consumer_key"));
    }
    
    @Test
    public void testAddOAuthParameter_edge() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        
        oauthRequest.addOAuthParameter("scope", "user");
        
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertTrue(oauthParameters.containsKey("scope"));
        assertEquals("user", oauthParameters.get("scope"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameter_error() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        
        oauthRequest.addOAuthParameter("invalid_key", "value");
    }
    
    @Test
    public void testCheckKey_typical() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        
        String checkedKey = oauthRequest.checkKey("oauth_consumer_key");
        assertEquals("oauth_consumer_key", checkedKey);
        
        checkedKey = oauthRequest.checkKey("scope");
        assertEquals("scope", checkedKey);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_error() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        
        oauthRequest.checkKey("invalid_key");
    }
    
    @Test
    public void testToString_typical() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        
        String expectedString = "@OAuthRequest(GET, http://example.com)";
        assertEquals(expectedString, oauthRequest.toString());
    }
}