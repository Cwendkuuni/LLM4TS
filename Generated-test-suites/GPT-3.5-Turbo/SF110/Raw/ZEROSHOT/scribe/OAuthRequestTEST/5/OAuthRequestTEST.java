import org.junit.*;
import static org.junit.Assert.*;
import org.scribe.model.*;

public class OAuthRequestTest {

    private OAuthRequest oauthRequest;
    
    @Before
    public void setUp() {
        oauthRequest = new OAuthRequest(Verb.GET, "https://example.com");
    }
    
    @Test
    public void testAddOAuthParameter() {
        oauthRequest.addOAuthParameter("testKey", "testValue");
        assertEquals("testValue", oauthRequest.getOauthParameters().get("testKey"));
    }
    
    @Test
    public void testAddOAuthParameterWithInvalidKey() {
        try {
            oauthRequest.addOAuthParameter("invalidKey", "testValue");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }
    
    @Test
    public void testGetOauthParameters() {
        oauthRequest.addOAuthParameter("testKey", "testValue");
        assertEquals("testValue", oauthRequest.getOauthParameters().get("testKey"));
    }
    
    @Test
    public void testToString() {
        assertEquals("@OAuthRequest(GET, https://example.com)", oauthRequest.toString());
    }
}