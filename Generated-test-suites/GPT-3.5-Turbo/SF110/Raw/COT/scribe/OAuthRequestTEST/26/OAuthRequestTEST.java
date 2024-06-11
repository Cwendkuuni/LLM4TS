import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

public class OAuthRequestTest {

    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertNotNull(request);
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
    }
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");
        request.addOAuthParameter("test", "value");
        assertEquals("value", request.getOauthParameters().get("test"));
    }
    
    @Test
    public void testCheckKey_validKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertEquals("oauth_test", request.checkKey("oauth_test"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_invalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.checkKey("invalid_key");
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertNotNull(request.getOauthParameters());
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
    }

}