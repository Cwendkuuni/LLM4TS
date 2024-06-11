package org.scribe.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthRequestTest {

    private OAuthRequest oauthRequest;
    
    @Before
    public void setUp() {
        oauthRequest = new OAuthRequest(Verb.GET, "https://example.com");
    }
    
    @Test
    public void testAddOAuthParameter() {
        oauthRequest.addOAuthParameter("testKey", "testValue");
        assertEquals("testValue", oauthRequest.getOauthParameters().get("testKey"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKey_invalidKey() {
        oauthRequest.checkKey("invalidKey");
    }
    
    @Test
    public void testCheckKey_validKey() {
        String result = oauthRequest.checkKey("oauth_validKey");
        assertEquals("oauth_validKey", result);
    }
    
    @Test
    public void testGetOauthParameters() {
        assertEquals(0, oauthRequest.getOauthParameters().size());
    }
    
    @Test
    public void testToString() {
        String result = oauthRequest.toString();
        assertEquals("@OAuthRequest(GET, https://example.com)", result);
    }
}