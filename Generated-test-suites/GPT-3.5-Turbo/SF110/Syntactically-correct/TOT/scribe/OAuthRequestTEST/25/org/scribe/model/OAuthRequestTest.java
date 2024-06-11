import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.scribe.model.*;
import java.util.*;

public class OAuthRequestTest {
    
    private OAuthRequest oAuthRequest;
    
    @Before
    public void setUp() {
        oAuthRequest = new OAuthRequest(Verb.GET, "http://example.com");
    }
    
    @After
    public void tearDown() {
        oAuthRequest = null;
    }
    
    // Test case for the constructor
    @Test
    public void testConstructor() {
        Assert.assertNotNull(oAuthRequest);
        Assert.assertEquals(Verb.GET, oAuthRequest.getVerb());
        Assert.assertEquals("http://example.com", oAuthRequest.getUrl());
    }
    
    // Test case for addOAuthParameter method - typical use case
    @Test
    public void testAddOAuthParameter() {
        oAuthRequest.addOAuthParameter("oauth_token", "abc123");
        Map<String, String> oauthParams = oAuthRequest.getOauthParameters();
        Assert.assertEquals(1, oauthParams.size());
        Assert.assertEquals("abc123", oauthParams.get("oauth_token"));
    }
    
    // Test case for addOAuthParameter method - edge case (empty key)
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterEmptyKey() {
        oAuthRequest.addOAuthParameter("", "value");
    }
    
    // Test case for addOAuthParameter method - edge case (empty value)
    @Test
    public void testAddOAuthParameterEmptyValue() {
        oAuthRequest.addOAuthParameter("key", "");
        Map<String, String> oauthParams = oAuthRequest.getOauthParameters();
        Assert.assertEquals(1, oauthParams.size());
        Assert.assertEquals("", oauthParams.get("key"));
    }
    
    // Test case for addOAuthParameter method - error scenario (invalid key)
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterInvalidKey() {
        oAuthRequest.addOAuthParameter("invalid_key", "value");
    }
    
    // Test case for checkKey method - typical use case
    @Test
    public void testCheckKey() {
        Assert.assertEquals("oauth_key", oAuthRequest.checkKey("oauth_key"));
        Assert.assertEquals("scope", oAuthRequest.checkKey("scope"));
    }
    
    // Test case for checkKey method - error scenario (invalid key)
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalidKey() {
        oAuthRequest.checkKey("invalid_key");
    }
    
    // Test case for getOauthParameters method - typical use case
    @Test
    public void testGetOauthParameters() {
        Map<String, String> oauthParams = new HashMap<>();
        oauthParams.put("param1", "value1");
        oauthParams.put("param2", "value2");
        
        oAuthRequest.addOAuthParameter("param1", "value1");
        oAuthRequest.addOAuthParameter("param2", "value2");
        
        Map<String, String> retrievedParams = oAuthRequest.getOauthParameters();
        
        Assert.assertEquals(oauthParams, retrievedParams);
    }
    
    // Test case for toString method - typical use case
    @Test
    public void testToString() {
        String expectedString = "@OAuthRequest(GET, http://example.com)";
        Assert.assertEquals(expectedString, oAuthRequest.toString());
    }
}