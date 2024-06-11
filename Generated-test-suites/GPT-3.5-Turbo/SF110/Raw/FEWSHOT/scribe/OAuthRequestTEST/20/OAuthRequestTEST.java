package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class OAuthRequestTest {
    
    @Test
    public void testConstructor() {
        OAuthRequest req = new OAuthRequest(Verb.GET, "http://example.com");
        assertNotNull(req);
        assertEquals(Verb.GET, req.getVerb());
        assertEquals("http://example.com", req.getUrl());
        assertNotNull(req.getOauthParameters());
        assertTrue(req.getOauthParameters().isEmpty());
    }
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest req = new OAuthRequest(Verb.GET, "http://example.com");
        req.addOAuthParameter("oauth_token", "12345");
        req.addOAuthParameter("scope", "read");
        
        assertNotNull(req.getOauthParameters());
        assertEquals(2, req.getOauthParameters().size());
        
        assertTrue(req.getOauthParameters().containsKey("oauth_token"));
        assertTrue(req.getOauthParameters().containsValue("12345"));
        
        assertTrue(req.getOauthParameters().containsKey("scope"));
        assertTrue(req.getOauthParameters().containsValue("read"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameter_invalidKey() {
        OAuthRequest req = new OAuthRequest(Verb.GET, "http://example.com");
        req.addOAuthParameter("invalid_key", "value");
    }
    
    @Test
    public void testCheckKey_validKey() {
        OAuthRequest req = new OAuthRequest(Verb.GET, "http://example.com");
        String key = "scope";
        
        String result = req.checkKey(key);
        
        assertEquals(key, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_invalidKey() {
        OAuthRequest req = new OAuthRequest(Verb.GET, "http://example.com");
        String key = "invalid_key";
        
        req.checkKey(key);
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest req = new OAuthRequest(Verb.GET, "http://example.com");
        assertNotNull(req.getOauthParameters());
        assertTrue(req.getOauthParameters().isEmpty());
        
        req.addOAuthParameter("oauth_token", "12345");
        req.addOAuthParameter("scope", "read");
        
        assertEquals(2, req.getOauthParameters().size());
    }
    
    @Test
    public void testToString() {
        OAuthRequest req = new OAuthRequest(Verb.GET, "http://example.com");
        String expected = "@OAuthRequest(GET, http://example.com)";
        
        assertEquals(expected, req.toString());
    }
}