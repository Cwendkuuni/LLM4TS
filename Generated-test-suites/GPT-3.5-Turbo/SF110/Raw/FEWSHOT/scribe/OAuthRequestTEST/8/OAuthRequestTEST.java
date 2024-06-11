package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class OAuthRequestTest {
    
    private OAuthRequest request;
    
    @Before
    public void setup() {
        request = new OAuthRequest(Verb.GET, "http://example.com");
    }
    
    @Test
    public void testAddOAuthParameter() {
        request.addOAuthParameter("key1", "value1");
        request.addOAuthParameter("key2", "value2");
        
        assertEquals(2, request.getOauthParameters().size());
        assertEquals("value1", request.getOauthParameters().get("key1"));
        assertEquals("value2", request.getOauthParameters().get("key2"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameter_invalidKey() {
        request.addOAuthParameter("not_oauth_key", "value");
    }
    
    @Test
    public void testCheckKey() {
        String validKey1 = "oauth_key";
        String validKey2 = "scope";
        String invalidKey = "not_oauth_key";
        
        assertEquals(validKey1, request.checkKey(validKey1));
        assertEquals(validKey2, request.checkKey(validKey2));
        
        try {
            request.checkKey(invalidKey);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }
    
    @Test
    public void testGetOauthParameters() {
        assertNotNull(request.getOauthParameters());
        assertTrue(request.getOauthParameters().isEmpty());
    }
    
    @Test
    public void testToString() {
        assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
        
        OAuthRequest postRequest = new OAuthRequest(Verb.POST, "http://example.com");
        assertEquals("@OAuthRequest(POST, http://example.com)", postRequest.toString());
    }
}