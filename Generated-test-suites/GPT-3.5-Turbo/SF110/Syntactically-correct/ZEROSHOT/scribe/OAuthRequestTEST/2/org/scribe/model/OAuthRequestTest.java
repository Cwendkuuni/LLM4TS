import org.junit.*;
import static org.junit.Assert.*;
import org.scribe.model.*;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        request.addOAuthParameter("key1", "value1");
        
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertNotNull(oauthParameters);
        assertEquals(1, oauthParameters.size());
        assertEquals("value1", oauthParameters.get("key1"));
    }
    
    @Test
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        try {
            request.addOAuthParameter("key2", "value2");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
        
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertNotNull(oauthParameters);
        assertEquals(0, oauthParameters.size());
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        Map<String, String> oauthParameters = request.getOauthParameters();
        
        assertNotNull(oauthParameters);
        assertEquals(0, oauthParameters.size());
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        String toString = request.toString();
        
        assertEquals("@OAuthRequest(GET, https://www.example.com)", toString);
    }
}