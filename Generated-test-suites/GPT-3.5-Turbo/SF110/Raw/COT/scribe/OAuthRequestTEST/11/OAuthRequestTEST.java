
import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthRequestTest {

    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "exampleKey");
        request.addOAuthParameter("scope", "exampleScope");
        request.addOAuthParameter("oauth_token", "exampleToken");
        
        assertEquals(3, request.getOauthParameters().size());
        assertEquals("exampleKey", request.getOauthParameters().get("oauth_consumer_key"));
        assertEquals("exampleScope", request.getOauthParameters().get("scope"));
        assertEquals("exampleToken", request.getOauthParameters().get("oauth_token"));
    }

    @Test
    public void testAddOAuthParameter_InvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        
        try {
            request.addOAuthParameter("invalid_key", "exampleValue");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "exampleKey");
        request.addOAuthParameter("scope", "exampleScope");
        
        Map<String, String> parameters = request.getOauthParameters();
        assertNotNull(parameters);
        assertEquals(2, parameters.size());
        assertEquals("exampleKey", parameters.get("oauth_consumer_key"));
        assertEquals("exampleScope", parameters.get("scope"));
    }

    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
    }
}

