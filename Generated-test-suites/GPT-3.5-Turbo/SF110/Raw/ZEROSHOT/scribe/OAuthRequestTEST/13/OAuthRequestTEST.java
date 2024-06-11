package org.scribe.model;

import org.junit.*;
import java.util.*;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        request.addOAuthParameter("key1", "value1");
        
        Map<String, String> oauthParameters = request.getOauthParameters();
        Assert.assertEquals(oauthParameters.get("key1"), "value1");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_InvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        request.checkKey("invalid_key");
    }
    
    @Test
    public void testCheckKey_ValidKey() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        String result = request.checkKey("oauth_consumer_key");
        Assert.assertEquals(result, "oauth_consumer_key");
    }
    
    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        request.addOAuthParameter("key1", "value1");
        request.addOAuthParameter("key2", "value2");
        
        Map<String, String> oauthParameters = request.getOauthParameters();
        Assert.assertEquals(oauthParameters.size(), 2);
        Assert.assertTrue(oauthParameters.containsKey("key1"));
        Assert.assertTrue(oauthParameters.containsKey("key2"));
        Assert.assertEquals(oauthParameters.get("key1"), "value1");
        Assert.assertEquals(oauthParameters.get("key2"), "value2");
    }
    
    @Test
    public void testToString() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://www.example.com");
        String result = request.toString();
        Assert.assertEquals(result, "@OAuthRequest(GET, http://www.example.com)");
    }
}