
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.scribe.model.*;

public class OAuthRequestTest {

    @Test
    public void testConstructor() {
        Verb verb = Verb.GET;
        String url = "http://example.com";
        OAuthRequest oauthRequest = new OAuthRequest(verb, url);
        
        assertEquals(verb, oauthRequest.getVerb());
        assertEquals(url, oauthRequest.getUrl());
        assertEquals(0, oauthRequest.getOauthParameters().size());
    }
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        
        oauthRequest.addOAuthParameter("oauth_key", "oauth_value");
        
        assertEquals(1, oauthRequest.getOauthParameters().size());
        assertEquals("oauth_value", oauthRequest.getOauthParameters().get("oauth_key"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameter_InvalidKey() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        
        oauthRequest.addOAuthParameter("invalid_key", "oauth_value");
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        
        assertEquals(0, oauthParameters.size());
    }
    
    @Test
    public void testToString() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
        
        String expected = "@OAuthRequest(GET, http://example.com)";
        assertEquals(expected, oauthRequest.toString());
    }
}

