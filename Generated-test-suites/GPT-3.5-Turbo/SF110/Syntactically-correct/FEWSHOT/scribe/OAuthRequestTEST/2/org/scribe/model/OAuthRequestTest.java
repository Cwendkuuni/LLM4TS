package org.scribe.model;

import static org.junit.Assert.*;

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
        oauthRequest.addOAuthParameter("oauth_token", "token");
        oauthRequest.addOAuthParameter("oauth_consumer_key", "consumer_key");
        oauthRequest.addOAuthParameter("oauth_signature_method", "HMAC-SHA1");
        
        Map<String, String> expectedParameters = new HashMap<>();
        expectedParameters.put("oauth_token", "token");
        expectedParameters.put("oauth_consumer_key", "consumer_key");
        expectedParameters.put("oauth_signature_method", "HMAC-SHA1");
        
        assertEquals(expectedParameters, oauthRequest.getOauthParameters());
    }
    
    @Test
    public void testCheckKey_withValidKeyStartingWithOAuthPrefix() {
        String actualKey = oauthRequest.checkKey("oauth_token");
        assertEquals("oauth_token", actualKey);
    }
    
    @Test
    public void testCheckKey_withValidKeyEqualToScope() {
        String actualKey = oauthRequest.checkKey("scope");
        assertEquals("scope", actualKey);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_withInvalidKey() {
        oauthRequest.checkKey("invalid_key");
    }
    
    @Test
    public void testToString() {
        String expectedToString = "@OAuthRequest(GET, http://example.com)";
        assertEquals(expectedToString, oauthRequest.toString());
    }
}