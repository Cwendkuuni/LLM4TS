package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class OAuthRequestTest {
    
    private OAuthRequest oauthRequest;
    
    @Before
    public void setUp() {
        oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
    }
    
    @Test
    public void testAddOAuthParameter() {
        oauthRequest.addOAuthParameter("oauth_token", "12345");
        oauthRequest.addOAuthParameter("oauth_consumer_key", "abcdef");
        
        Map<String, String> oauthParams = oauthRequest.getOauthParameters();
        assertNotNull(oauthParams);
        assertEquals(2, oauthParams.size());
        assertTrue(oauthParams.containsKey("oauth_token"));
        assertTrue(oauthParams.containsKey("oauth_consumer_key"));
        assertEquals("12345", oauthParams.get("oauth_token"));
        assertEquals("abcdef", oauthParams.get("oauth_consumer_key"));
    }
    
    @Test
    public void testAddOAuthParameterWithInvalidKey() {
        try {
            oauthRequest.addOAuthParameter("invalid_key", "12345");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }
    
    @Test
    public void testCheckKeyWithValidKey() {
        String validKey1 = "oauth_token";
        String validKey2 = "scope";
        
        assertEquals(validKey1, oauthRequest.checkKey(validKey1));
        assertEquals(validKey2, oauthRequest.checkKey(validKey2));
    }
    
    @Test
    public void testCheckKeyWithInvalidKey() {
        try {
            oauthRequest.checkKey("invalid_key");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }
    
    @Test
    public void testToString() {
        assertEquals("@OAuthRequest(GET, http://example.com)", oauthRequest.toString());
    }
}