import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter_ValidKeyAndValue_UpdatesOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        
        request.addOAuthParameter("oauth_consumer_key", "abc");
        
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertTrue(oauthParameters.containsKey("oauth_consumer_key"));
        assertEquals("abc", oauthParameters.get("oauth_consumer_key"));
    }
    
    @Test
    public void testAddOAuthParameter_InvalidKey_ThrowsIllegalArgumentException() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        
        try {
            request.addOAuthParameter("invalid_key", "value");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }
    
    @Test
    public void testCheckKey_ValidKey_ReturnsSameKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        
        String key = request.checkKey("oauth_consumer_key");
        
        assertEquals("oauth_consumer_key", key);
    }
    
    @Test
    public void testCheckKey_InvalidKey_ThrowsIllegalArgumentException() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        
        try {
            request.checkKey("invalid_key");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }
    
    @Test
    public void testGetOauthParameters_NoParameters_ReturnsEmptyMap() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertNotNull(oauthParameters);
        assertTrue(oauthParameters.isEmpty());
    }
    
    @Test
    public void testToString_ReturnsFormattedString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        
        String toString = request.toString();
        
        assertEquals("@OAuthRequest(GET, https://example.com)", toString);
    }
}