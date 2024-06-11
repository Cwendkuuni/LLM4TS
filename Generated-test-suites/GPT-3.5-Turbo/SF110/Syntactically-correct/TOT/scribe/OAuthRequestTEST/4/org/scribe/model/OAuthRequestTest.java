
import org.junit.Assert;
import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;
import java.util.Map;
import java.util.HashMap;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        
        // Typical Use Case
        request.addOAuthParameter("oauth_token", "token");
        Map<String, String> expectedOAuthParameters = new HashMap<>();
        expectedOAuthParameters.put("oauth_token", "token");
        Assert.assertEquals(expectedOAuthParameters, request.getOauthParameters());
        
        // Edge Case - Adding OAuth parameter with key "scope"
        request.addOAuthParameter("scope", "full");
        expectedOAuthParameters.put("scope", "full");
        Assert.assertEquals(expectedOAuthParameters, request.getOauthParameters());
        
        // Error Scenario - Adding invalid OAuth parameter key
        try {
            request.addOAuthParameter("invalid_key", "value");
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        Map<String, String> oauthParameters = request.getOauthParameters();
        
        // Typical Use Case - Initially, there should be no OAuth parameters
        Assert.assertEquals(0, oauthParameters.size());
        
        // Edge Case - Adding OAuth parameter
        request.addOAuthParameter("oauth_token", "token");
        Assert.assertEquals(1, oauthParameters.size());
        Assert.assertEquals("token", oauthParameters.get("oauth_token"));
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        
        // Typical Use Case
        String expectedToString = "@OAuthRequest(GET, http://example.com)";
        Assert.assertEquals(expectedToString, request.toString());
        
        // Edge Case - Different Verb and URL
        request = new OAuthRequest(Verb.POST, "https://example.org");
        expectedToString = "@OAuthRequest(POST, https://example.org)";
        Assert.assertEquals(expectedToString, request.toString());
    }
}
