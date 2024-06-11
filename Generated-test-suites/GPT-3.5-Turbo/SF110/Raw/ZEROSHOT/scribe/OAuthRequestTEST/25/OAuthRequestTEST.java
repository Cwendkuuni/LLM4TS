package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class OAuthRequestTest {

    private OAuthRequest oauthRequest;
    
    @Before
    public void setup() {
        oauthRequest = new OAuthRequest(Verb.GET, "https://www.example.com");
    }
    
    @Test
    public void testAddOAuthParameter() {
        String key = "oauth_key";
        String value = "oauth_value";
        
        oauthRequest.addOAuthParameter(key, value);
        
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertTrue(oauthParameters.containsKey(key));
        assertEquals(value, oauthParameters.get(key));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterInvalidKey() {
        String key = "invalid_key";
        String value = "oauth_value";
        
        oauthRequest.addOAuthParameter(key, value);
    }
    
    @Test
    public void testGetOauthParameters() {
        Map<String, String> expectedParams = new HashMap<String, String>();
        
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        assertTrue(oauthParameters.equals(expectedParams));
    }
    
    @Test
    public void testToString() {
        String expectedToString = "@OAuthRequest(GET, https://www.example.com)";
        
        String toString = oauthRequest.toString();
        assertEquals(expectedToString, toString);
    }
}