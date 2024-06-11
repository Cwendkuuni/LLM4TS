import org.junit.*;
import static org.junit.Assert.*;

public class OAuthRequestTest {

    // Test case for the constructor
    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
        assertNotNull(request.getOauthParameters());
    }
    
    // Test case for addOAuthParameter method
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        
        request.addOAuthParameter("oauth_token", "12345");
        
        assertTrue(request.getOauthParameters().containsKey("oauth_token"));
        assertEquals("12345", request.getOauthParameters().get("oauth_token"));
    }
    
    // Test case for checkKey method when key starts with "oauth_"
    @Test
    public void testCheckKeyStartsWithOAuth() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        
        String key = "oauth_consumer_key";
        assertEquals(key, request.checkKey(key));
    }
    
    // Test case for checkKey method when key is "scope"
    @Test
    public void testCheckKeyIsScope() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        
        String key = "scope";
        assertEquals(key, request.checkKey(key));
    }
    
    // Test case for checkKey method when key is invalid
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalid() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        
        String key = "invalid_key";
        request.checkKey(key);
    }
    
    // Test case for toString method
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        
        String expected = "@OAuthRequest(GET, http://example.com)";
        assertEquals(expected, request.toString());
    }
}