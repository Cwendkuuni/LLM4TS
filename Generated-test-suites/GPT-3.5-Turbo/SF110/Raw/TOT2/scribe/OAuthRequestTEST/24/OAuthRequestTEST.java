import static org.junit.Assert.*;
import org.junit.Test;

public class OAuthRequestTest {

    private OAuthRequest oAuthRequest;
    
    @Before
    public void setUp() {
        oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
    }
    
    @Test
    public void testAddOAuthParameter() {
        oAuthRequest.addOAuthParameter("test_key", "test_value");
        
        assertTrue(oAuthRequest.getOauthParameters().containsKey("test_key"));
        assertEquals("test_value", oAuthRequest.getOauthParameters().get("test_key"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalid() {
        oAuthRequest.addOAuthParameter("invalid_key", "test_value");
    }
    
    @Test
    public void testCheckKeyValid() {
        String validKey = oAuthRequest.checkKey("oauth_valid_key");
        
        assertEquals("oauth_valid_key", validKey);
    }
    
    @Test
    public void testToString() {
        assertEquals("@OAuthRequest(GET, http://example.com)", oAuthRequest.toString());
    }
}