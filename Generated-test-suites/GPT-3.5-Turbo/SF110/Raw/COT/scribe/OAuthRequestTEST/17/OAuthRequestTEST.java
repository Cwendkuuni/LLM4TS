
import org.junit.*;
import static org.junit.Assert.*;

public class OAuthRequestTest {

    @Test
    public void testConstructor() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        
        OAuthRequest oauthRequest = new OAuthRequest(verb, url);
        
        assertEquals(verb, oauthRequest.getVerb());
        assertEquals(url, oauthRequest.getUrl());
        assertNotNull(oauthRequest.getOauthParameters());
    }
    
    @Test
    public void testAddOAuthParameter() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        String key = "oauth_consumer_key";
        String value = "12345";
        
        OAuthRequest oauthRequest = new OAuthRequest(verb, url);
        oauthRequest.addOAuthParameter(key, value);
        
        assertTrue(oauthRequest.getOauthParameters().containsKey(key));
        assertEquals(value, oauthRequest.getOauthParameters().get(key));
    }
    
    @Test
    public void testAddOAuthParameter_IllegalKey() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        String key = "invalid_key";
        String value = "12345";
        
        OAuthRequest oauthRequest = new OAuthRequest(verb, url);
        
        try {
            oauthRequest.addOAuthParameter(key, value);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }
    
    @Test
    public void testGetOauthParameters() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        
        OAuthRequest oauthRequest = new OAuthRequest(verb, url);
        
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        
        assertNotNull(oauthParameters);
        assertTrue(oauthParameters.isEmpty());
    }
    
    @Test
    public void testToString() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        
        OAuthRequest oauthRequest = new OAuthRequest(verb, url);
        
        assertEquals("@OAuthRequest(GET, https://example.com)", oauthRequest.toString());
    }
    
}
