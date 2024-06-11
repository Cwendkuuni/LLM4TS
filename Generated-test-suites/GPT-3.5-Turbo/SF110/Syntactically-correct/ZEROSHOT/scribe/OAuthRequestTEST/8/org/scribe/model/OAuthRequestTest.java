package org.scribe.model;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class OAuthRequestTest {
    
    @Test
    public void testConstructor() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("https://example.com", request.getUrl());
        assertTrue(request.getOauthParameters().isEmpty());
    }
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("oauth_key", "value");
        assertEquals("value", request.getOauthParameters().get("oauth_key"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("invalid_key", "value");
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        assertEquals(0, request.getOauthParameters().size());
        
        request.addOAuthParameter("oauth_key1", "value1");
        request.addOAuthParameter("oauth_key2", "value2");
        assertEquals(2, request.getOauthParameters().size());
        assertEquals("value1", request.getOauthParameters().get("oauth_key1"));
        assertEquals("value2", request.getOauthParameters().get("oauth_key2"));
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        assertEquals("@OAuthRequest(GET, https://example.com)", request.toString());
    }
}