
import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

import static org.junit.Assert.*;

import java.util.Map;


public class OAuthRequestTest {
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com/api");
        request.addOAuthParameter("oauth_consumer_key", "consumer_key");
        
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals(1, oauthParameters.size());
        assertTrue(oauthParameters.containsKey("oauth_consumer_key"));
        assertEquals("consumer_key", oauthParameters.get("oauth_consumer_key"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameter_withNonValidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com/api");
        request.addOAuthParameter("invalid_key", "value");
    }
    
    @Test
    public void testAddOAuthParameter_withValidScopeKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com/api");
        request.addOAuthParameter("scope", "read write");
        
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals(1, oauthParameters.size());
        assertTrue(oauthParameters.containsKey("scope"));
        assertEquals("read write", oauthParameters.get("scope"));
    }
    
    @Test
    public void testCheckKey_withValidKeyStartingWithOAuth() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com/api");
        
        String validKey = request.checkKey("oauth_consumer_key");
        assertEquals("oauth_consumer_key", validKey);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_withInvalidKeyStartingWithOAuth() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com/api");
        
        request.checkKey("not_oauth_key");
    }
    
    @Test
    public void testCheckKey_withValidScopeKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com/api");
        
        String validKey = request.checkKey("scope");
        assertEquals("scope", validKey);
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com/api");
        
        String expectedString = "@OAuthRequest(GET, https://example.com/api)";
        assertEquals(expectedString, request.toString());
    }
}
