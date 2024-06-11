
package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class OAuthRequestTest {

    private OAuthRequest request;

    @Before
    public void setUp() {
        request = new OAuthRequest(Verb.GET, "https://example.com");
    }

    @Test
    public void testAddOAuthParameter() {
        request.addOAuthParameter("oauth_token", "token");
        request.addOAuthParameter("oauth_consumer_key", "consumerKey");
        request.addOAuthParameter("scope", "read");

        Map<String, String> parameters = request.getOauthParameters();

        assertEquals("token", parameters.get("oauth_token"));
        assertEquals("consumerKey", parameters.get("oauth_consumer_key"));
        assertEquals("read", parameters.get("scope"));
    }

    @Test
    public void testCheckKey_validKey() {
        String key = "oauth_token";
        String checkedKey = request.checkKey(key);
        assertEquals(key, checkedKey);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_invalidKey() {
        request.checkKey("invalid_key");
    }

    @Test
    public void testToString() {
        String expectedString = "@OAuthRequest(GET, https://example.com)";
        assertEquals(expectedString, request.toString());
    }
}
