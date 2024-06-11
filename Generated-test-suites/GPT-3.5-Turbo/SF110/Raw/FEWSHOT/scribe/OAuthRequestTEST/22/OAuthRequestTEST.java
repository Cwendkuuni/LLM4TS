
package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class OAuthRequestTest {

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://example.com");

        request.addOAuthParameter("oauth_consumer_key", "1234567890");
        request.addOAuthParameter("oauth_token", "abcdef123456");
        request.addOAuthParameter("oauth_signature", "XyZ123456");
        request.addOAuthParameter("scope", "read write");

        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("oauth_consumer_key", "1234567890");
        expectedParams.put("oauth_token", "abcdef123456");
        expectedParams.put("oauth_signature", "XyZ123456");

        assertEquals(expectedParams, request.getOauthParameters());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://example.com");
        
        request.addOAuthParameter("invalid_key", "value");
    }

    @Test
    public void testCheckKeyWithValidKey() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://example.com");
        
        String key = "oauth_consumer_key";
        String checkedKey = request.checkKey(key);
        
        assertEquals(key, checkedKey);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyWithInvalidKey() {
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://example.com");
        
        String key = "invalid_key";
        request.checkKey(key);
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com");

        Map<String, String> oauthParams = request.getOauthParameters();

        assertNotNull(oauthParams);
        assertTrue(oauthParams.isEmpty());
    }

    @Test
    public void testToString() {
        Verb verb = Verb.PUT;
        String url = "https://example.com";
        String expectedString = "@OAuthRequest(" + verb + ", " + url + ")";

        OAuthRequest request = new OAuthRequest(verb, url);

        assertEquals(expectedString, request.toString());
    }
}
