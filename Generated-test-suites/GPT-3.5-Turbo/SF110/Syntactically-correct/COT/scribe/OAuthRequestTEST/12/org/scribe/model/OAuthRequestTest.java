import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        
        // Test adding a new OAuth parameter
        request.addOAuthParameter("oauth_token", "abcd1234");
        assertEquals("abcd1234", request.getOauthParameters().get("oauth_token"));
        
        // Test adding an existing OAuth parameter with a different value
        request.addOAuthParameter("oauth_token", "efgh5678");
        assertEquals("efgh5678", request.getOauthParameters().get("oauth_token"));
        
        // Test adding a non-OAuth parameter
        request.addOAuthParameter("name", "John");
        assertNull(request.getOauthParameters().get("name"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");

        // Test valid key starting with "oauth_"
        request.checkKey("oauth_token");
        
        // Test valid key equal to "scope"
        request.checkKey("scope");
        
        // Test valid key not starting with "oauth_" or equal to "scope"
        request.checkKey("name");
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        assertEquals("@OAuthRequest(GET, https://example.com)", request.toString());
    }
}