import org.junit.*;
import org.scribe.model.*;

import java.util.*;

public class OAuthRequestTest {

    private OAuthRequest request;

    @Before
    public void setUp() {
        request = new OAuthRequest(Verb.GET, "http://example.com");
    }
  
    @Test
    public void testAddOAuthParameter() {
        request.addOAuthParameter("testKey", "testValue");
        Map<String, String> oauthParams = request.getOauthParameters();
        Assert.assertEquals(oauthParams.get("oauth_testKey"), "testValue");
    }
  
    @Test
    public void testAddOAuthParameterWithInvalidKey() {
        try {
            request.addOAuthParameter("invalidKey", "testValue");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "OAuth parameters must either be 'scope' or start with 'oauth_'");
        }
    }
  
    @Test
    public void testAddOAuthParameterWithValidKey() {
        request.addOAuthParameter("oauth_testKey", "testValue");
        Map<String, String> oauthParams = request.getOauthParameters();
        Assert.assertEquals(oauthParams.get("oauth_testKey"), "testValue");
    }
  
    @Test
    public void testAddOAuthParameterWithScopeKey() {
        request.addOAuthParameter("scope", "testValue");
        Map<String, String> oauthParams = request.getOauthParameters();
        Assert.assertEquals(oauthParams.get("scope"), "testValue");
    }
  
    @Test
    public void testGetOauthParameters() {
        Map<String, String> oauthParams = request.getOauthParameters();
        Assert.assertTrue(oauthParams.isEmpty());
    }
  
    @Test
    public void testToString() {
        String expectedString = "@OAuthRequest(GET, http://example.com)";
        Assert.assertEquals(request.toString(), expectedString);
    }
}