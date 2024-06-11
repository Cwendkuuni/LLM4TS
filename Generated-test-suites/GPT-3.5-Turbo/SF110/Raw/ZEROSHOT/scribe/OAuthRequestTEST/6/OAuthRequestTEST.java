package org.scribe.model;

import java.util.*;
import static org.junit.Assert.*;
import org.junit.*;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        request.addOAuthParameter("key1", "value1");
        request.addOAuthParameter("key2", "value2");
        
        Map<String, String> oauthParameters = request.getOauthParameters();
        assertNotNull(oauthParameters);
        assertEquals("value1", oauthParameters.get("key1"));
        assertEquals("value2", oauthParameters.get("key2"));
    }
    
    @Test
    public void testCheckKey_ValidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        String key = "oauth_test";
        String checkedKey = request.checkKey(key);
        
        assertEquals(key, checkedKey);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_InvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        String key = "invalid_key";
        request.checkKey(key);
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        Map<String, String> oauthParameters = request.getOauthParameters();
        
        assertNotNull(oauthParameters);
        assertTrue(oauthParameters.isEmpty());
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");
        String expectedToString = "@OAuthRequest(GET, https://example.com)";
        String requestToString = request.toString();
        
        assertEquals(expectedToString, requestToString);
    }
}