package org.scribe.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OAuthRequestTest {

    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("https://www.example.com", request.getUrl());
        assertEquals(0, request.getOauthParameters().size());
    }
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        request.addOAuthParameter("oauth_token", "TOKEN123");
        request.addOAuthParameter("scope", "read");
        
        assertEquals(2, request.getOauthParameters().size());
        assertEquals("TOKEN123", request.getOauthParameters().get("oauth_token"));
        assertEquals("read", request.getOauthParameters().get("scope"));
    }
    
    @Test
    public void testAddOAuthParameter_InvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        
        try {
            request.addOAuthParameter("invalid_key", "value");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.example.com");
        
        assertEquals("@OAuthRequest(GET, https://www.example.com)", request.toString());
    }
}