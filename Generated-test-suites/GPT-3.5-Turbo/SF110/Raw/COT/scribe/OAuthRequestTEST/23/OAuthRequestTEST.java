import org.junit.*;
import static org.junit.Assert.*;

public class OAuthRequestTest {
    
    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://example.com");
        assertEquals(request.getVerb(), Verb.POST);
        assertEquals(request.getUrl(), "https://example.com");
        assertNotNull(request.getOauthParameters());
    }
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://example.com");
        request.addOAuthParameter("oauth_token", "12345");
        request.addOAuthParameter("scope", "read");
        assertEquals(request.getOauthParameters().get("oauth_token"), "12345");
        assertEquals(request.getOauthParameters().get("scope"), "read");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://example.com");
        request.addOAuthParameter("invalid_key", "value");
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://example.com");
        assertEquals(request.getOauthParameters(), new HashMap<String, String>());
        request.addOAuthParameter("oauth_token", "12345");
        assertEquals(request.getOauthParameters().get("oauth_token"), "12345");
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://example.com");
        String expected = "@OAuthRequest(POST, https://example.com)";
        assertEquals(request.toString(), expected);
    }
}