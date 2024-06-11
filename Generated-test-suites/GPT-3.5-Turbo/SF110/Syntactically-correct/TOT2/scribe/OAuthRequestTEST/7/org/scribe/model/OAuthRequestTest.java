package org.scribe.model;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

import java.util.Map;

public class OAuthRequestTest {
    
    private OAuthRequest oauthRequest;
    
    @Before
    public void setUp() {
        oauthRequest = new OAuthRequest(Verb.GET, "http://example.com");
    }
    
    @Test
    public void testAddOAuthParameter() {
        oauthRequest.addOAuthParameter("oauth_consumer_key", "consumerKey123");
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        
        assertTrue(oauthParameters.containsKey("oauth_consumer_key"));
        assertEquals("consumerKey123", oauthParameters.get("oauth_consumer_key"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterInvalidKey() {
        oauthRequest.addOAuthParameter("invalid_key", "value");
    }
    
    @Test
    public void testCheckKeyValid() {
        String checkedKey = oauthRequest.checkKey("oauth_nonce");
        assertEquals("oauth_nonce", checkedKey);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckKeyInvalid() {
        oauthRequest.checkKey("invalid_key");
    }
    
    @Test
    public void testGetOauthParameters() {
        Map<String, String> oauthParameters = oauthRequest.getOauthParameters();
        
        assertNotNull(oauthParameters);
        assertEquals(0, oauthParameters.size());
    }
    
    @Test
    public void testToString() {
        assertEquals("@OAuthRequest(GET, http://example.com)", oauthRequest.toString());
    }
}