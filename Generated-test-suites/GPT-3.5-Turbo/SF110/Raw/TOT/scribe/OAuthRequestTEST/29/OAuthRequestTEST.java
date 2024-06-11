import org.junit.*;
import java.util.*;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://www.example.com");
        oauthRequest.addOAuthParameter("oauth_version", "1.0");
        
        Map<String, String> expectedParams = new HashMap<String, String>();
        expectedParams.put("oauth_version", "1.0");
        
        Assert.assertEquals(expectedParams, oauthRequest.getOauthParameters());
    }
    
    @Test
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://www.example.com");
        
        try {
            oauthRequest.addOAuthParameter("invalid_key", "value");
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("OAuth parameters must either be 'scope' or start with 'oauth_'"));
        }
    }
    
    @Test
    public void testCheckKeyWithValidKey() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://www.example.com");
        
        String key = oauthRequest.checkKey("oauth_version");
        Assert.assertEquals("oauth_version", key);
    }
    
    @Test
    public void testCheckKeyWithScopeKey() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://www.example.com");
        
        String key = oauthRequest.checkKey("scope");
        Assert.assertEquals("scope", key);
    }
    
    @Test
    public void testCheckKeyWithInvalidKey() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://www.example.com");
        
        try {
            oauthRequest.checkKey("invalid_key");
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("OAuth parameters must either be 'scope' or start with 'oauth_'"));
        }
    }
    
    @Test
    public void testToString() {
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://www.example.com");
        String expectedString = "@OAuthRequest(GET, https://www.example.com)";
        
        Assert.assertEquals(expectedString, oauthRequest.toString());
    }
}