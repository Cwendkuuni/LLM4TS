
import static org.junit.Assert.*;
import org.junit.*;

public class OAuthRequestTest {

    OAuthRequest request;

    @Before
    public void setUp() {
        request = new OAuthRequest(Verb.GET, "https://example.com");
    }

    @Test
    public void testAddOAuthParameter() {
        request.addOAuthParameter("oauth_version", "1.0");
        assertEquals(1, request.getOauthParameters().size());
        assertEquals("1.0", request.getOauthParameters().get("oauth_version"));
    }

    @Test
    public void testAddOAuthParameterWithScope() {
        request.addOAuthParameter("scope", "read write");
        assertEquals(1, request.getOauthParameters().size());
        assertEquals("read write", request.getOauthParameters().get("scope"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterInvalidKey() {
        request.addOAuthParameter("invalid_key", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterInvalidKeyStartsWithOauth() {
        request.addOAuthParameter("oauth_invalid_key", "value");
    }

    @Test
    public void testGetOauthParameters() {
        assertTrue(request.getOauthParameters().isEmpty());
        request.addOAuthParameter("oauth_version", "1.0");
        request.addOAuthParameter("oauth_token", "token123");
        assertFalse(request.getOauthParameters().isEmpty());
        assertEquals(2, request.getOauthParameters().size());
        assertTrue(request.getOauthParameters().containsKey("oauth_version"));
        assertTrue(request.getOauthParameters().containsKey("oauth_token"));
        assertEquals("1.0", request.getOauthParameters().get("oauth_version"));
        assertEquals("token123", request.getOauthParameters().get("oauth_token"));
    }

    @Test
    public void testToString() {
        request.addOAuthParameter("oauth_version", "1.0");
        String expected = "@OAuthRequest(GET, https://example.com)";
        assertEquals(expected, request.toString());
    }

}

