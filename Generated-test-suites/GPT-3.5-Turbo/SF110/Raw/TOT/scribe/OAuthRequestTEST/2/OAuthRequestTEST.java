import org.junit.*;
import static org.junit.Assert.*;

public class OAuthRequestTest {
  
    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
        assertNotNull(request.getOauthParameters());
        assertTrue(request.getOauthParameters().isEmpty());
        assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");
        request.addOAuthParameter("oauth_token", "1234567890");
        assertEquals(1, request.getOauthParameters().size());
        assertEquals("1234567890", request.getOauthParameters().get("oauth_token"));
        
        request.addOAuthParameter("scope", "read write");
        assertEquals(2, request.getOauthParameters().size());
        assertEquals("read write", request.getOauthParameters().get("scope"));
        
        try {
            request.addOAuthParameter("invalid_key", "invalid_value");
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("OAuth parameters must either be 'scope' or start with 'oauth_'"));
        }
    }

    @Test
    public void testCheckKey() {
        OAuthRequest request = new OAuthRequest(Verb.PUT, "http://example.com");
        
        assertEquals("oauth_token", request.checkKey("oauth_token"));
        assertEquals("scope", request.checkKey("scope"));
        
        try {
            request.checkKey("invalid_key");
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("OAuth parameters must either be 'scope' or start with 'oauth_'"));
        }
    }
}