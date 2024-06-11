import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class OAuthRequestTest {

    private OAuthRequest oauthRequest;

    @Before
    public void setUp() {
        oauthRequest = new OAuthRequest(Verb.GET, "https://www.example.com");
    }

    @Test
    public void testAddOAuthParameter_typical() {
        oauthRequest.addOAuthParameter("oauth_token", "123456789");
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertEquals(1, oauthParameters.size());
        assertTrue(oauthParameters.containsKey("oauth_token"));
        assertEquals("123456789", oauthParameters.get("oauth_token"));
    }

    @Test
    public void testAddOAuthParameter_edge() {
        oauthRequest.addOAuthParameter("oauth_", "abcd");
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertEquals(1, oauthParameters.size());
        assertTrue(oauthParameters.containsKey("oauth_"));
        assertEquals("abcd", oauthParameters.get("oauth_"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameter_error() {
        oauthRequest.addOAuthParameter("invalid_key", "value");
    }

    @Test
    public void testGetOauthParameters_typical() {
        oauthRequest.addOAuthParameter("oauth_token", "123456789");
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertEquals(1, oauthParameters.size());
        assertTrue(oauthParameters.containsKey("oauth_token"));
        assertEquals("123456789", oauthParameters.get("oauth_token"));
    }

    @Test
    public void testGetOauthParameters_empty() {
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertNotNull(oauthParameters);
        assertTrue(oauthParameters.isEmpty());
    }

    @Test
    public void testCheckKey_typical() {
        String key = oauthRequest.checkKey("oauth_token");
        assertEquals("oauth_token", key);
    }

    @Test
    public void testCheckKey_scopeKey() {
        String key = oauthRequest.checkKey("scope");
        assertEquals("scope", key);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_invalidKey() {
        oauthRequest.checkKey("invalid_key");
    }

    @Test
    public void testToString() {
        String expectedString = "@OAuthRequest(GET, https://www.example.com)";
        String toString = oauthRequest.toString();
        assertEquals(expectedString, toString);
    }
}