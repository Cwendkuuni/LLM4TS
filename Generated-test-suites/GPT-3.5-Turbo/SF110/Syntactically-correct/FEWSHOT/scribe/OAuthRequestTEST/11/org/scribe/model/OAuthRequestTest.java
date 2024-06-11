
package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

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
        request.addOAuthParameter("oauth_token", "token");
        assertEquals("token", request.getOauthParameters().get("oauth_token"));
    }

    @Test
    public void testAddOAuthParameterWithScopeKey() {
        request.addOAuthParameter("scope", "scope_value");
        assertEquals("scope_value", request.getOauthParameters().get("scope"));
    }

    @Test
    public void testAddOAuthParameterWithInvalidKeyThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            request.addOAuthParameter("invalid_key", "value");
        });
    }

    @Test
    public void testCheckKeyWithValidKey() {
        assertEquals("oauth_token", request.checkKey("oauth_token"));
    }

    @Test
    public void testCheckKeyWithScopeKey() {
        assertEquals("scope", request.checkKey("scope"));
    }

    @Test
    public void testCheckKeyWithInvalidKeyThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            request.checkKey("invalid_key");
        });
    }

    @Test
    public void testToString() {
        assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
    }
}
