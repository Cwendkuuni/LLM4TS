import static org.junit.Assert.*;
import org.junit.Test;
import org.scribe.model.*;

public class OAuthRequestTest {
    
    @Test
    public void testAddOAuthParameter_validParameter_addedSuccessfully() {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "https://www.example.com");
        oAuthRequest.addOAuthParameter("oauth_token", "12345");
        assertEquals("12345", oAuthRequest.getOauthParameters().get("oauth_token"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameter_invalidParameter_throwsIllegalArgumentException() {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "https://www.example.com");
        oAuthRequest.addOAuthParameter("invalid_key", "value");
    }
    
    @Test
    public void testCheckKey_validKey_returnsKey() {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "https://www.example.com");
        String key = oAuthRequest.checkKey("oauth_token");
        assertEquals("oauth_token", key);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_invalidKey_throwsIllegalArgumentException() {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "https://www.example.com");
        oAuthRequest.checkKey("invalid_key");
    }
    
    @Test
    public void testGetOauthParameters_emptyParameters_returnsEmptyMap() {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "https://www.example.com");
        assertTrue(oAuthRequest.getOauthParameters().isEmpty());
    }
    
    @Test
    public void testToString_returnsFormattedString() {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "https://www.example.com");
        assertEquals("@OAuthRequest(GET, https://www.example.com)", oAuthRequest.toString());
    }
}