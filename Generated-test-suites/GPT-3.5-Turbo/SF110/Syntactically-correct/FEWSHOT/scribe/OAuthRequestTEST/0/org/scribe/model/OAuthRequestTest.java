package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class OAuthRequestTest {
    // Test addOAuthParameter() method
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        
        // Add parameters
        request.addOAuthParameter("oauth_token", "12345");
        request.addOAuthParameter("oauth_consumer_key", "ABCDE");
        
        // Check if parameters are added correctly
        assertEquals("12345", request.getOauthParameters().get("oauth_token"));
        assertEquals("ABCDE", request.getOauthParameters().get("oauth_consumer_key"));
    }
    
    // Test checkKey() method
    @Test
    public void testCheckKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        
        // Test valid keys
        assertEquals("oauth_token", request.checkKey("oauth_token"));
        assertEquals("oauth_consumer_key", request.checkKey("oauth_consumer_key"));
        assertEquals("scope", request.checkKey("scope"));
        
        // Test invalid keys
        try {
            request.checkKey("invalid_key");
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    // Test getOauthParameters() method
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        
        // Add parameters
        request.addOAuthParameter("oauth_token", "12345");
        request.addOAuthParameter("oauth_consumer_key", "ABCDE");
        
        // Get parameters
        Map<String, String> parameters = request.getOauthParameters();
        
        // Check if parameters are returned correctly
        assertEquals("12345", parameters.get("oauth_token"));
        assertEquals("ABCDE", parameters.get("oauth_consumer_key"));
    }
    
    // Test toString() method
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com/api");
        
        // Check string representation
        assertEquals("@OAuthRequest(GET, http://www.example.com/api)", request.toString());
        
        // Check string representation after modifying the request
        request.addOAuthParameter("oauth_token", "12345");
        assertEquals("@OAuthRequest(GET, http://www.example.com/api)", request.toString());
    }
}