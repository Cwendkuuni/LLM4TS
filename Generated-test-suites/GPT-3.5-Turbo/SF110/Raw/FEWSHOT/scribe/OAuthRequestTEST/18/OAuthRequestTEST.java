package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class OAuthRequestTest {
    
    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        request.addOAuthParameter("key1", "value1");
        request.addOAuthParameter("key2", "value2");
        Map<String, String> expectedParameters = new HashMap<>();
        expectedParameters.put("key1", "value1");
        expectedParameters.put("key2", "value2");
        assertEquals(expectedParameters, request.getOauthParameters());
    }
    
    @Test
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        try {
            request.addOAuthParameter("invalid_key", "value");
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        Map<String, String> expectedParameters = new HashMap<>();
        assertEquals(expectedParameters, request.getOauthParameters());
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        assertEquals("@OAuthRequest(GET, http://www.example.com)", request.toString());
    }
}