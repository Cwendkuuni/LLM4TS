package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class OAuthRequestTest {

    private OAuthRequest request;
    
    @Before
    public void setUp() {
        request = new OAuthRequest(Verb.GET, "http://www.example.com");
    }
    
    @Test
    public void testAddOAuthParameter() {
        request.addOAuthParameter("oauth_consumer_key", "key1");
        request.addOAuthParameter("oauth_nonce", "nonce1");
        request.addOAuthParameter("oauth_timestamp", "1234567890");
        
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals(3, oauthParameters.size());
        assertEquals("key1", oauthParameters.get("oauth_consumer_key"));
        assertEquals("nonce1", oauthParameters.get("oauth_nonce"));
        assertEquals("1234567890", oauthParameters.get("oauth_timestamp"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKey() {
        request.addOAuthParameter("invalid_key", "value");
    }
    
    @Test
    public void testCheckKeyWithOAuthKey() {
        String key = "oauth_consumer_key";
        assertEquals(key, request.checkKey(key));
    }
    
    @Test
    public void testCheckKeyWithScopeKey() {
        String key = "scope";
        assertEquals(key, request.checkKey(key));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyWithInvalidKey() {
        String key = "invalid_key";
        request.checkKey(key);
    }
    
    @Test
    public void testGetOauthParameters() {
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertTrue(oauthParameters.isEmpty());
        
        oauthParameters.put("key1", "value1");
        
        oauthParameters = request.getOauthParameters();
        assertTrue(oauthParameters.isEmpty());
    }
    
    @Test
    public void testToString() {
        String expected = "@OAuthRequest(GET, http://www.example.com)";
        assertEquals(expected, request.toString());
    }
}