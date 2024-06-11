import org.junit.*;
import static org.junit.Assert.*;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://example.com");
        request.addOAuthParameter("oauth_consumer_key", "abc123");
        request.addOAuthParameter("oauth_token", "xyz456");
        
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals(2, oauthParameters.size());
        assertEquals("abc123", oauthParameters.get("oauth_consumer_key"));
        assertEquals("xyz456", oauthParameters.get("oauth_token"));
    }
    
    @Test
    public void testCheckKey_validKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        
        String validKey = "oauth_consumer_key";
        String checkedKey = request.checkKey(validKey);
        
        assertEquals(validKey, checkedKey);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_invalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        
        String invalidKey = "invalid_key";
        request.checkKey(invalidKey);
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.PUT, "https://example.com");
        Map<String, String> oauthParameters = request.getOauthParameters();
        
        assertNotNull(oauthParameters);
        assertEquals(0, oauthParameters.size());
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.DELETE, "https://example.com");
        String expectedString = "@OAuthRequest(DELETE, https://example.com)";
        
        assertEquals(expectedString, request.toString());
    }
}