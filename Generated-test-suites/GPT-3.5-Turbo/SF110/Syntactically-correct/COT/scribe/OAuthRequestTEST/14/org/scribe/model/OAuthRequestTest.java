
package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthRequestTest {
    
    @Test
    public void testAddOAuthParameter() {
        // Creating an instance of OAuthRequest
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        
        // Adding an OAuth parameter
        request.addOAuthParameter("oauth_token", "123456789");
        
        // Asserting that the parameter is added correctly
        assertEquals("123456789", request.getOauthParameters().get("oauth_token"));
    }
    
    @Test
    public void testAddOAuthParameter_InvalidKey() {
        // Creating an instance of OAuthRequest
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        
        // Adding an invalid OAuth parameter
        try {
            request.addOAuthParameter("invalid_key", "value");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // IllegalArgumentException should be thrown, test passed
        }
    }
    
    @Test
    public void testGetOauthParameters() {
        // Creating an instance of OAuthRequest
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        
        // Adding an OAuth parameter
        request.addOAuthParameter("oauth_token", "123456789");
        
        // Asserting that the added parameter is returned correctly
        assertEquals("123456789", request.getOauthParameters().get("oauth_token"));
    }
    
    @Test
    public void testToString() {
        // Creating an instance of OAuthRequest
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
        
        // Asserting the toString() representation
        assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());
    }
}
