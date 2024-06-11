import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthRequestTest {
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_token", "12345");
        
        assertEquals("12345", request.getOauthParameters().get("oauth_token"));
    }
    
    @Test
    public void testAddOAuthParameterInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");
        
        try {
            request.addOAuthParameter("key", "value");
            fail("Adding invalid key should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.PUT, "http://example.com");
        Map<String, String> oauthParams = request.getOauthParameters();
        
        assertTrue(oauthParams.isEmpty());
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.DELETE, "http://example.com");
        String expected = "@OAuthRequest(DELETE, http://example.com)";
        
        assertEquals(expected, request.toString());
    }

}