package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthRequestTest {
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        request.addOAuthParameter("test_key", "test_value");
        
        assertEquals("test_value", request.getOauthParameters().get("test_key"));
    }
    
    @Test
    public void testCheckKeyValidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        
        assertEquals("oauth_key", request.checkKey("oauth_key"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        
        request.checkKey("invalid_key");
    }
    
    @Test
    public void testGetOAuthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        request.addOAuthParameter("test_key", "test_value");
        
        assertEquals(1, request.getOauthParameters().size());
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        
        assertEquals("@OAuthRequest(GET, https://www.example.com)", request.toString());
    }
}