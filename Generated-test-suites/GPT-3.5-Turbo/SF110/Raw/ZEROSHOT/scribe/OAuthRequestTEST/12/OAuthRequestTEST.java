import org.junit.*;
import static org.junit.Assert.*;
import org.scribe.model.*;

public class OAuthRequestTest {

    private OAuthRequest oAuthRequest;
    
    @Before
    public void setUp() {
        oAuthRequest = new OAuthRequest(Verb.GET, "https://example.com/api");
    }
    
    @Test
    public void testAddOAuthParameter() {
        oAuthRequest.addOAuthParameter("oauth_token", "token123");
        assertEquals("token123", oAuthRequest.getOauthParameters().get("oauth_token"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterInvalidKey() {
        oAuthRequest.addOAuthParameter("invalid_key", "value");
    }
    
    @Test
    public void testGetOauthParameters() {
        oAuthRequest.addOAuthParameter("oauth_token", "token123");
        assertEquals("token123", oAuthRequest.getOauthParameters().get("oauth_token"));
    }
    
    @Test
    public void testToString() {
        String expectedString = "@OAuthRequest(GET, https://example.com/api)";
        assertEquals(expectedString, oAuthRequest.toString());
    }
    
    @After
    public void tearDown() {
        oAuthRequest = null;
    }
}