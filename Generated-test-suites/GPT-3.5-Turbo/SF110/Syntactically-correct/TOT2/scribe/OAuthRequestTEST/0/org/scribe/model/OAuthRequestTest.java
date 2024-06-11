package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthRequestTest {
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        request.addOAuthParameter("test_key", "test_value");
        
        assertEquals("test_value", request.getOauthParameters().get("test_key"));
    }
    
    @Test
    public void testCheckKeyValid() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        String key = "oauth_key";
        
        assertEquals(key, request.checkKey(key));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalid() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        String key = "invalid_key";
        
        request.checkKey(key);
    }
    
    @Test
    public void testGetOAuthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        request.addOAuthParameter("test_key", "test_value");
        
        assertEquals(1, request.getOauthParameters().size());
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        
        assertEquals("@OAuthRequest(GET, http://www.example.com)", request.toString());
    }
}