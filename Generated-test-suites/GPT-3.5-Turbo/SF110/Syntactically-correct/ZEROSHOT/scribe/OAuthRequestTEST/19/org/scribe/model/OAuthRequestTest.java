package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class OAuthRequestTest {

    private OAuthRequest oAuthRequest;

    @Before
    public void setUp() {
        oAuthRequest = new OAuthRequest(Verb.GET, "https://example.com");
    }

    @Test
    public void testAddOAuthParameter() {
        oAuthRequest.addOAuthParameter("testKey", "testValue");
        Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
        assertTrue(oauthParameters.containsKey("testKey"));
        assertEquals(oauthParameters.get("testKey"), "testValue");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalidKey() {
        oAuthRequest.checkKey("invalidKey");
    }

    @Test
    public void testCheckKeyValidKey() {
        String key = oAuthRequest.checkKey("oauth_validKey");
        assertEquals(key, "oauth_validKey");
    }

    @Test
    public void testCheckKeyValidScopeKey() {
        String key = oAuthRequest.checkKey("scope");
        assertEquals(key, "scope");
    }

    @Test
    public void testGetOauthParameters() {
        Map<String, String> oauthParameters = oAuthRequest.getOauthParameters();
        assertNotNull(oauthParameters);
        assertTrue(oauthParameters.isEmpty());
    }

    @Test
    public void testToString() {
        String expected = "@OAuthRequest(GET, https://example.com)";
        assertEquals(oAuthRequest.toString(), expected);
    }

}