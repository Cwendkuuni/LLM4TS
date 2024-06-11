package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class OAuthRequestTest {
    
    private OAuthRequest oauthRequest;
    
    @Before
    public void setup() {
        oauthRequest = new OAuthRequest(Verb.GET, "https://example.com/api");
    }
    
    @Test
    public void testAddOAuthParameter() {
        oauthRequest.addOAuthParameter("oauth_token", "abc123");
        
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertNotNull(oauthParameters);
        assertEquals(1, oauthParameters.size());
        assertTrue(oauthParameters.containsKey("oauth_token"));
        assertEquals("abc123", oauthParameters.get("oauth_token"));
    }
    
    @Test
    public void testAddOAuthParameter_invalidKey() {
        try {
            oauthRequest.addOAuthParameter("invalid_key", "abc123");
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OAuth parameters must either be 'scope' or start with 'oauth_'", e.getMessage());
        }
    }
    
    @Test
    public void testAddOAuthParameter_scopeKey() {
        oauthRequest.addOAuthParameter("scope", "read");
        
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertNotNull(oauthParameters);
        assertEquals(1, oauthParameters.size());
        assertTrue(oauthParameters.containsKey("scope"));
        assertEquals("read", oauthParameters.get("scope"));
    }
    
    @Test
    public void testGetOauthParameters() {
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertNotNull(oauthParameters);
        assertTrue(oauthParameters.isEmpty());
    }
    
    @Test
    public void testToString() {
        String expectedResult = "@OAuthRequest(GET, https://example.com/api)";
        assertEquals(expectedResult, oauthRequest.toString());
    }
}