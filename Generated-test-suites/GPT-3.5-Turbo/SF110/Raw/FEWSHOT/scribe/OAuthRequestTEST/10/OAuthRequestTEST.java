package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class OAuthRequestTest {
    private OAuthRequest oauthRequest;
    
    @Before
    public void setup() {
        oauthRequest = new OAuthRequest(Verb.GET, "https://api.example.com");
    }
    
    @Test
    public void testAddOAuthParameter() {
        oauthRequest.addOAuthParameter("oauth_token", "abcd1234");
        oauthRequest.addOAuthParameter("oauth_consumer_key", "consumerKey");
        
        Map<String, String> oauthParams = oauthRequest.getOauthParameters();
        
        assertEquals(2, oauthParams.size());
        assertEquals("abcd1234", oauthParams.get("oauth_token"));
        assertEquals("consumerKey", oauthParams.get("oauth_consumer_key"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameter_invalidKey() {
        oauthRequest.addOAuthParameter("invalid_key", "value");
    }
    
    @Test
    public void testCheckKey() {
        String key = "oauth_consumer_key";
        String validKey = oauthRequest.checkKey(key);
        
        assertEquals(key, validKey);
    }
    
    @Test
    public void testCheckKey_scope() {
        String key = "scope";
        String validKey = oauthRequest.checkKey(key);
        
        assertEquals(key, validKey);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_invalidKey() {
        oauthRequest.checkKey("invalid_key");
    }
    
    @Test
    public void testGetOauthParameters() {
        assertNotNull(oauthRequest.getOauthParameters());
        assertTrue(oauthRequest.getOauthParameters().isEmpty());
    }
    
    @Test
    public void testToString() {
        assertEquals("@OAuthRequest(GET, https://api.example.com)", oauthRequest.toString());
    }
}