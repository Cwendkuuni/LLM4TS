import org.junit.*;
import static org.junit.Assert.*;
import org.scribe.model.*;

public class OAuthRequestTest {

    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("https://example.com", request.getUrl());
    }
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_consumer_key", "testConsumerKey");
        request.addOAuthParameter("oauth_token", "testAccessToken");
        
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals(2, oauthParameters.size());
        assertTrue(oauthParameters.containsKey("oauth_consumer_key"));
        assertTrue(oauthParameters.containsKey("oauth_token"));
        assertEquals("testConsumerKey", oauthParameters.get("oauth_consumer_key"));
        assertEquals("testAccessToken", oauthParameters.get("oauth_token"));
    }
    
    @Test
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        
        try {
            request.addOAuthParameter("invalid_key", "testValue");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_consumer_key", "testConsumerKey");
        request.addOAuthParameter("oauth_token", "testAccessToken");
        
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertNotNull(oauthParameters);
        assertEquals(2, oauthParameters.size());
        assertTrue(oauthParameters.containsKey("oauth_consumer_key"));
        assertTrue(oauthParameters.containsKey("oauth_token"));
        assertEquals("testConsumerKey", oauthParameters.get("oauth_consumer_key"));
        assertEquals("testAccessToken", oauthParameters.get("oauth_token"));
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        assertEquals("@OAuthRequest(GET, https://example.com)", request.toString());
    }

}