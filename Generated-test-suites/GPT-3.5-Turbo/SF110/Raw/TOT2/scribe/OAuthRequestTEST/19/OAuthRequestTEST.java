import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthRequestTest {

    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertNotNull(request);
    }
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("testKey", "testValue");
        assertTrue(request.getOauthParameters().containsKey("testKey"));
        assertEquals("testValue", request.getOauthParameters().get("testKey"));
    }
    
    @Test
    public void testCheckKeyValid() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertEquals("oauth_testKey", request.checkKey("oauth_testKey"));
        assertEquals("scope", request.checkKey("scope"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalid() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.checkKey("invalidKey");
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
    }

}