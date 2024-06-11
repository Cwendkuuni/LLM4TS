import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class OAuthRequestTest {

    @Test
    public void testConstructor() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://example.com");
        assertEquals(Verb.GET, oauthRequest.getVerb());
        assertEquals("https://example.com", oauthRequest.getUrl());
        assertNotNull(oauthRequest.getOauthParameters());
    }
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://example.com");
        oauthRequest.addOAuthParameter("oauth_token", "abc123");
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertEquals("abc123", oauthParameters.get("oauth_token"));
    }
    
    @Test
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://example.com");
        try {
            oauthRequest.addOAuthParameter("invalid_key", "value");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }
    
    @Test
    public void testAddOAuthParameterWithScopeKey() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://example.com");
        oauthRequest.addOAuthParameter("scope", "read");
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertEquals("read", oauthParameters.get("scope"));
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://example.com");
        oauthRequest.addOAuthParameter("oauth_consumer_key", "consumer_key");
        oauthRequest.addOAuthParameter("oauth_nonce", "nonce123");
        oauthRequest.addOAuthParameter("oauth_timestamp", "1234567890");
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertEquals(3, oauthParameters.size());
        assertEquals("consumer_key", oauthParameters.get("oauth_consumer_key"));
        assertEquals("nonce123", oauthParameters.get("oauth_nonce"));
        assertEquals("1234567890", oauthParameters.get("oauth_timestamp"));
    }
    
    @Test
    public void testToString() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://example.com");
        assertEquals("@OAuthRequest(GET, https://example.com)", oauthRequest.toString());
    }

}