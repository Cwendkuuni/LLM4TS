import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("key", "value");
        assertEquals("value", request.getOauthParameters().get("key"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_StartsWithInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.checkKey("invalid_key");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_EqualsInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.checkKey("scope");
    }
    
    @Test
    public void testCheckKey_ValidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertEquals("oauth_key", request.checkKey("oauth_key"));
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("key1", "value1");
        request.addOAuthParameter("key2", "value2");
        
        assertEquals("value1", request.getOauthParameters().get("key1"));
        assertEquals("value2", request.getOauthParameters().get("key2"));
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
    }
}