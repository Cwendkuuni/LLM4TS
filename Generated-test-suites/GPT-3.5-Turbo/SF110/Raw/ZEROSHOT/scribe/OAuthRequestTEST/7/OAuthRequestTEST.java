package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        request.addOAuthParameter("oauth_consumer_key", "consumer_key");
        
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertEquals(1, oauthParameters.size());
        assertEquals("consumer_key", oauthParameters.get("oauth_consumer_key"));
    }
    
    @Test
    public void testAddOAuthParameter_InvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        
        try {
            request.addOAuthParameter("invalid_key", "value");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }
    
    // Test other methods and constructors as required
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        String expected = "@OAuthRequest(GET, http://example.com)";
        assertEquals(expected, request.toString());
    }
}