package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class OAuthRequestTest {
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");
        request.addOAuthParameter("oauth_token", "123456");
        request.addOAuthParameter("scope", "read");
        
        assertEquals("123456", request.getOauthParameters().get("oauth_token"));
        assertEquals("read", request.getOauthParameters().get("scope"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameter_invalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");
        request.addOAuthParameter("invalid_key", "value");
    }
    
    @Test
    public void testCheckKey_validKey() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");
        
        assertEquals("oauth_token", request.checkKey("oauth_token"));
        assertEquals("scope", request.checkKey("scope"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_invalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");
        request.checkKey("invalid_key");
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");
        request.addOAuthParameter("oauth_token", "123456");
        request.addOAuthParameter("scope", "read");
        
        assertEquals(2, request.getOauthParameters().size());
        assertTrue(request.getOauthParameters().containsKey("oauth_token"));
        assertTrue(request.getOauthParameters().containsKey("scope"));
        assertEquals("123456", request.getOauthParameters().get("oauth_token"));
        assertEquals("read", request.getOauthParameters().get("scope"));
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "http://example.com");
        
        assertEquals("@OAuthRequest(POST, http://example.com)", request.toString());
    }
}