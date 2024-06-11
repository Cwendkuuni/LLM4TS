package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OAuthRequestTest {
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_token", "123456789");
        request.addOAuthParameter("oauth_consumer_key", "abcdefg");
        request.addOAuthParameter("scope", "read write");
        
        assertTrue(request.getOauthParameters().containsKey("oauth_token"));
        assertTrue(request.getOauthParameters().containsKey("oauth_consumer_key"));
        assertTrue(request.getOauthParameters().containsKey("scope"));
        
        assertEquals("123456789", request.getOauthParameters().get("oauth_token"));
        assertEquals("abcdefg", request.getOauthParameters().get("oauth_consumer_key"));
        assertEquals("read write", request.getOauthParameters().get("scope"));
    }
    
    @Test
    public void testAddOAuthParameter_invalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        
        try {
            request.addOAuthParameter("invalid_key", "123456789");
            assertTrue(false);
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testCheckKey_validKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        
        String result = request.checkKey("oauth_token");
        assertEquals("oauth_token", result);
        
        result = request.checkKey("scope");
        assertEquals("scope", result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_invalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        
        request.checkKey("invalid_key");
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_token", "123456789");
        request.addOAuthParameter("oauth_consumer_key", "abcdefg");
        request.addOAuthParameter("scope", "read write");
        
        assertEquals(3, request.getOauthParameters().size());
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        String result = request.toString();
        
        assertTrue(result.contains("@OAuthRequest(GET, https://example.com)"));
    }
}