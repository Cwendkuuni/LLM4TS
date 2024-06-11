
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

public class OAuthRequestTest {
    
    private OAuthRequest oauthRequest;
    
    @Before
    public void setUp() {
        oauthRequest = new OAuthRequest(Verb.GET, "https://example.com");
    }
    
    @Test
    public void testAddOAuthParameter() {
        oauthRequest.addOAuthParameter("oauth_token", "123456789");
        oauthRequest.addOAuthParameter("oauth_consumer_key", "key123");
        oauthRequest.addOAuthParameter("scope", "read");
        
        assertEquals("123456789", oauthRequest.getOauthParameters().get("oauth_token"));
        assertEquals("key123", oauthRequest.getOauthParameters().get("oauth_consumer_key"));
        assertEquals("read", oauthRequest.getOauthParameters().get("scope"));
        
        assertThrows(IllegalArgumentException.class, () -> oauthRequest.addOAuthParameter("invalid", "value123"));
    }
    
    @Test
    public void testCheckKey() {
        assertEquals("oauth_token", oauthRequest.checkKey("oauth_token"));
        assertEquals("oauth_consumer_key", oauthRequest.checkKey("oauth_consumer_key"));
        assertEquals("scope", oauthRequest.checkKey("scope"));
        
        assertThrows(IllegalArgumentException.class, () -> oauthRequest.checkKey("invalid_key"));
    }
    
    @Test
    public void testToString() {
        assertEquals("@OAuthRequest(GET, https://example.com)", oauthRequest.toString());
    }
}
