import static org.junit.Assert.*;
import org.junit.Test;

public class OAuthRequestTest {
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("key", "value");
        
        assertEquals("value", request.getOauthParameters().get("key"));
    }
    
    @Test
    public void testAddOAuthParameter_invalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        
        try {
            request.addOAuthParameter("invalid", "value");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        
        assertEquals(0, request.getOauthParameters().size());
        
        request.addOAuthParameter("key1", "value1");
        request.addOAuthParameter("key2", "value2");
        
        assertEquals(2, request.getOauthParameters().size());
        assertEquals("value1", request.getOauthParameters().get("key1"));
        assertEquals("value2", request.getOauthParameters().get("key2"));
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        
        assertEquals("@OAuthRequest(GET, https://example.com)", request.toString());
    }
}