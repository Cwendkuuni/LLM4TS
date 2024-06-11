import org.junit.*;
import static org.junit.Assert.*;
import org.scribe.model.*;

public class OAuthRequestTest {

    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        assertNotNull(request);
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("https://example.com", request.getUrl());
        assertTrue(request.getOauthParameters().isEmpty());
    }
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://example.com");
        request.addOAuthParameter("oauth_token", "TOKEN123");
        request.addOAuthParameter("oauth_consumer_key", "CONSUMERKEY456");
        
        assertEquals(2, request.getOauthParameters().size());
        assertTrue(request.getOauthParameters().containsKey("oauth_token"));
        assertTrue(request.getOauthParameters().containsKey("oauth_consumer_key"));
        assertEquals("TOKEN123", request.getOauthParameters().get("oauth_token"));
        assertEquals("CONSUMERKEY456", request.getOauthParameters().get("oauth_consumer_key"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.PUT, "https://example.com");
        request.addOAuthParameter("invalid_key", "VALUE");
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        assertEquals(0, request.getOauthParameters().size());
        
        request.addOAuthParameter("oauth_token", "TOKEN123");
        request.addOAuthParameter("oauth_consumer_key", "CONSUMERKEY456");
        
        assertEquals(2, request.getOauthParameters().size());
        assertTrue(request.getOauthParameters().containsKey("oauth_token"));
        assertTrue(request.getOauthParameters().containsKey("oauth_consumer_key"));
        assertEquals("TOKEN123", request.getOauthParameters().get("oauth_token"));
        assertEquals("CONSUMERKEY456", request.getOauthParameters().get("oauth_consumer_key"));
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://example.com");
        String expectedString = "@OAuthRequest(POST, https://example.com)";
        assertEquals(expectedString, request.toString());
    }
}