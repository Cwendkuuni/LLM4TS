package org.scribe.model;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OAuthRequestTest {

    private OAuthRequest oauthRequest;

    @Before
    public void setUp() {
        oauthRequest = new OAuthRequest(Verb.POST, "http://example.com/api");
    }

    @Test
    public void testAddOAuthParameter() {
        oauthRequest.addOAuthParameter("oauth_consumer_key", "zzxx1234");
        assertEquals("zzxx1234", oauthRequest.getOauthParameters().get("oauth_consumer_key"));
    }

    @Test
    public void testCheckKeyValid() {
        assertEquals("oauth_consumer_key", oauthRequest.checkKey("oauth_consumer_key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalid() {
        oauthRequest.checkKey("invalid_key");
    }

    @Test
    public void testGetOauthParameters() {
        assertEquals(0, oauthRequest.getOauthParameters().size());
    }

    @Test
    public void testToString() {
        assertEquals("@OAuthRequest(POST, http://example.com/api)", oauthRequest.toString());
    }

    @Test
    public void testAddOAuthParameterEdgeCase() {
        oauthRequest.addOAuthParameter("scope", "limited");
        assertEquals("limited", oauthRequest.getOauthParameters().get("scope"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyEdgeCase() {
        oauthRequest.checkKey("scope");
    }

}