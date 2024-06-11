import static org.junit.Assert.*;
import org.junit.*;

public class OAuthRequestTest {
  
    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("https://example.com", request.getUrl());
        assertNotNull(request.getOauthParameters());
    }
  
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://example.com");
        request.addOAuthParameter("oauth_consumer_key", "123456");
        request.addOAuthParameter("oauth_token", "abcdef");
        assertEquals("123456", request.getOauthParameters().get("oauth_consumer_key"));
        assertEquals("abcdef", request.getOauthParameters().get("oauth_token"));
    }
  
    @Test
    public void testCheckKeyValidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        String key = "oauth_consumer_key";
        assertEquals(key, request.checkKey(key));
    }
  
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        String key = "invalid_key";
        request.checkKey(key);
    }
  
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        assertNotNull(request.getOauthParameters());
    }
  
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        assertEquals("@OAuthRequest(GET, https://example.com)", request.toString());
    }
}