import org.junit.*;
import static org.junit.Assert.*;

public class OAuthRequestTest {

    private OAuthRequest request;

    @Before
    public void setUp() {
        request = new OAuthRequest(Verb.GET, "http://example.com");
    }

    @Test
    public void testAddOAuthParameter() {
        request.addOAuthParameter("oauth_token", "123456789");
        assertEquals("123456789", request.getOauthParameters().get("oauth_token"));
    }

    @Test
    public void testAddOAuthParameterInvalidKey() {
        try {
            request.addOAuthParameter("invalid_key", "value");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }

    @Test
    public void testCheckKeyValidKey() {
        String key = request.checkKey("oauth_nonce");
        assertEquals("oauth_nonce", key);
    }

    @Test
    public void testCheckKeyInvalidKey() {
        try {
            request.checkKey("invalid_key");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }

    @Test
    public void testToString() {
        String expected = "@OAuthRequest(GET, http://example.com)";
        assertEquals(expected, request.toString());
    }

}