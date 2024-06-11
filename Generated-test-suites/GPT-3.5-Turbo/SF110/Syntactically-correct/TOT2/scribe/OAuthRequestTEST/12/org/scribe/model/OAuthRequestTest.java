package org.scribe.model;
import static org.junit.Assert.*;
import org.junit.Test;

public class OAuthRequestTest {
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "key123");
        
        assertEquals("key123", request.getOauthParameters().get("oauth_consumer_key"));
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
        request.addOAuthParameter("oauth_consumer_key", "key123");
        
        assertTrue(request.getOauthParameters().containsKey("oauth_consumer_key"));
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        
        assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
    }
}