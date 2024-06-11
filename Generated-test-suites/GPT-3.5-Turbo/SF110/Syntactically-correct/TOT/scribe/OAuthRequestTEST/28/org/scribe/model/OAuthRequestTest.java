import static org.junit.Assert.*;
import org.junit.*;

public class OAuthRequestTest {
    
    private OAuthRequest oauthRequest;

    @Before
    public void setup() {
        oauthRequest = new OAuthRequest(Verb.GET, "https://example.com");
    }

    @Test
    public void testAddOAuthParameter_typicalCase() {
        oauthRequest.addOAuthParameter("oauth_token", "123456789");
        oauthRequest.addOAuthParameter("oauth_consumer_key", "abcdefg");
        oauthRequest.addOAuthParameter("oauth_signature", "xyz123");
        assertEquals(3, oauthRequest.getOauthParameters().size());
        assertTrue(oauthRequest.getOauthParameters().containsKey("oauth_token"));
        assertTrue(oauthRequest.getOauthParameters().containsKey("oauth_consumer_key"));
        assertTrue(oauthRequest.getOauthParameters().containsValue("123456789"));
        assertTrue(oauthRequest.getOauthParameters().containsValue("abcdefg"));
    }

    @Test
    public void testAddOAuthParameter_edgeCase() {
        oauthRequest.addOAuthParameter("scope", "email");
        assertEquals(1, oauthRequest.getOauthParameters().size());
        assertTrue(oauthRequest.getOauthParameters().containsKey("scope"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameter_errorCase() {
        oauthRequest.addOAuthParameter("invalid_key", "value");
    }

    @Test
    public void testGetOauthParameters_typicalCase() {
        oauthRequest.addOAuthParameter("oauth_token", "123456789");
        oauthRequest.addOAuthParameter("oauth_consumer_key", "abcdefg");
        assertEquals(2, oauthRequest.getOauthParameters().size());
        assertTrue(oauthRequest.getOauthParameters().containsKey("oauth_token"));
        assertTrue(oauthRequest.getOauthParameters().containsKey("oauth_consumer_key"));
    }

    @Test
    public void testToString_typicalCase() {
        String expectedString = "@OAuthRequest(GET, https://example.com)";
        assertEquals(expectedString, oauthRequest.toString());
    }
}