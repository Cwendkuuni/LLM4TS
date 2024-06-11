import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import java.util.HashMap;

public class OAuthRequestTest {

    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("https://www.example.com", request.getUrl());
        assertNotNull(request.getOauthParameters());
        assertTrue(request.getOauthParameters().isEmpty());
    }
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        
        request.addOAuthParameter("oauth_consumer_key", "123456789");
        
        Map<String, String> expectedParameters = new HashMap<>();
        expectedParameters.put("oauth_consumer_key", "123456789");
        
        assertEquals(expectedParameters, request.getOauthParameters());
    }
    
    @Test
    public void testCheckKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        
        // Valid key
        assertEquals("oauth_consumer_key", request.checkKey("oauth_consumer_key"));
        
        // Valid key starting with "oauth_"
        assertEquals("oauth_consumer_secret", request.checkKey("oauth_consumer_secret"));
        
        // Invalid key
        try {
            request.checkKey("invalid_key");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        
        assertEquals("@OAuthRequest(GET, https://www.example.com)", request.toString());
    }
}