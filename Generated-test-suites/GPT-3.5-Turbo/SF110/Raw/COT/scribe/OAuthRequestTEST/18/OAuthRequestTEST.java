import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthRequestTest {
    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
        assertNotNull(request.getOauthParameters());
        assertTrue(request.getOauthParameters().isEmpty());
    }
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("testKey", "testValue");
        assertEquals("testValue", request.getOauthParameters().get("testKey"));
    }
    
    @Test
    public void testCheckKeyValidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        String key = request.checkKey("oauth_consumer_key");
        assertEquals("oauth_consumer_key", key);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.checkKey("invalid_key");
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("testKey", "testValue");
        assertEquals(1, request.getOauthParameters().size());
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
    }
}