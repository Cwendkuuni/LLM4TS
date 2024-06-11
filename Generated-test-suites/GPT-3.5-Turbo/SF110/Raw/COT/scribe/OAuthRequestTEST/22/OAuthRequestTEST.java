import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        
        // Basic functionality test
        request.addOAuthParameter("oauth_key", "oauth_value");
        assertTrue(request.getOauthParameters().containsKey("oauth_key"));
        assertEquals("oauth_value", request.getOauthParameters().get("oauth_key"));
        
        // Test exception handling
        try {
            request.addOAuthParameter("key", "value");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        
        // Basic functionality test
        assertTrue(request.getOauthParameters().isEmpty());
        
        // Add parameters and test
        request.addOAuthParameter("oauth_key1", "oauth_value1");
        request.addOAuthParameter("oauth_key2", "oauth_value2");
        
        assertEquals(2, request.getOauthParameters().size());
        assertTrue(request.getOauthParameters().containsKey("oauth_key1"));
        assertEquals("oauth_value1", request.getOauthParameters().get("oauth_key1"));
        assertTrue(request.getOauthParameters().containsKey("oauth_key2"));
        assertEquals("oauth_value2", request.getOauthParameters().get("oauth_key2"));
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        
        // Basic functionality test
        assertEquals("@OAuthRequest(GET, https://example.com)", request.toString());
    }
}