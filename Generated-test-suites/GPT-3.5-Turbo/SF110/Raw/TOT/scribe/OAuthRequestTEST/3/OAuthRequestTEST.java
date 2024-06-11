
package org.scribe.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthRequestTest {

    private OAuthRequest request;

    @Before
    public void setUp() {
        request = new OAuthRequest(Verb.GET, "https://example.com");
    }

    @Test
    public void testAddOAuthParameter() {
        request.addOAuthParameter("oauth_token", "12345");
        request.addOAuthParameter("scope", "read write");
        request.addOAuthParameter("extra", "value");
        
        assertTrue(request.getOauthParameters().containsKey("oauth_token"));
        assertTrue(request.getOauthParameters().containsKey("scope"));
        assertTrue(request.getOauthParameters().containsKey("extra"));
        
        assertEquals("12345", request.getOauthParameters().get("oauth_token"));
        assertEquals("read write", request.getOauthParameters().get("scope"));
        assertEquals("value", request.getOauthParameters().get("extra"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_invalidKey() {
        request.addOAuthParameter("invalid_key", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_invalidKeyScope() {
        request.addOAuthParameter("scope", "value");
    }

    @Test
    public void testCheckKey_validKeyStartWithOAuth() {
        String key = request.checkKey("oauth_parameter");
        assertEquals("oauth_parameter", key);
    }

    @Test
    public void testCheckKey_validKeyEqualsScope() {
        String key = request.checkKey("scope");
        assertEquals("scope", key);
    }

    @Test
    public void testToString() {
        String expectedString = "@OAuthRequest(GET, https://example.com)";
        assertEquals(expectedString, request.toString());
    }
}
