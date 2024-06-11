package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class OAuthRequestTest {
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        
        request.addOAuthParameter("oauth_token", "abc123");
        assertTrue(request.getOauthParameters().containsKey("oauth_token"));
        assertEquals("abc123", request.getOauthParameters().get("oauth_token"));
        
        request.addOAuthParameter("scope", "read write");
        assertTrue(request.getOauthParameters().containsKey("scope"));
        assertEquals("read write", request.getOauthParameters().get("scope"));
        
        request.addOAuthParameter("other_key", "other_value");
        assertTrue(request.getOauthParameters().containsKey("other_key"));
        assertEquals("other_value", request.getOauthParameters().get("other_key"));
        
        try {
            request.addOAuthParameter("invalid_key", "invalid_value");
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://example.com");
        assertEquals("@OAuthRequest(POST, https://example.com)", request.toString());
        
        request = new OAuthRequest(Verb.PUT, "https://example.org");
        assertEquals("@OAuthRequest(PUT, https://example.org)", request.toString());
        
        request = new OAuthRequest(Verb.DELETE, "https://example.net");
        assertEquals("@OAuthRequest(DELETE, https://example.net)", request.toString());
    }
}