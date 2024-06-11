import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthRequestTest {
    
    @Test
    public void testAddOAuthParameter_typical() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");
        request.addOAuthParameter("oauth_token", "TOKEN123");
        assertEquals("TOKEN123", request.getOauthParameters().get("oauth_token"));
    }
    
    @Test
    public void testAddOAuthParameter_edgeCase() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");
        request.addOAuthParameter("scope", "public");
        assertEquals("public", request.getOauthParameters().get("scope"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameter_error() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");
        request.addOAuthParameter("invalid_key", "value");
    }
    
    @Test
    public void testCheckKey_typical() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");
        String validKey = request.checkKey("oauth_signature");
        assertEquals("oauth_signature", validKey);
    }
    
    @Test
    public void testCheckKey_edgeCase() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");
        String validKey = request.checkKey("scope");
        assertEquals("scope", validKey);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_error() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");
        request.checkKey("invalid_key");
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");
        String expected = "@OAuthRequest(POST, http://example.com)";
        assertEquals(expected, request.toString());
    }
}