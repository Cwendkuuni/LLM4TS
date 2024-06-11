package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testGetToken() {
        // Typical use case
        Token token1 = new Token("token1", "secret1");
        assertEquals("token1", token1.getToken());
        
        // Edge case: empty token
        Token token2 = new Token("", "secret2");
        assertEquals("", token2.getToken());
    }
    
    @Test
    public void testGetSecret() {
        // Typical use case
        Token token1 = new Token("token1", "secret1");
        assertEquals("secret1", token1.getSecret());
        
        // Edge case: empty secret
        Token token2 = new Token("token2", "");
        assertEquals("", token2.getSecret());
    }
    
    @Test
    public void testGetRawResponse() {
        // Typical use case: rawResponse is not null
        Token token1 = new Token("token1", "secret1", "response1");
        assertEquals("response1", token1.getRawResponse());
        
        // Error scenario: rawResponse is null
        Token token2 = new Token("token2", "secret2");
        try {
            token2.getRawResponse();
            fail("Expected IllegalStateException not thrown");
        } catch (IllegalStateException e) {
            assertEquals("This token object was not constructed by scribe and does not have a rawResponse", e.getMessage());
        }
    }
    
    @Test
    public void testToString() {
        // Typical use case
        Token token1 = new Token("token1", "secret1");
        assertEquals("Token[token1 , secret1]", token1.toString());
        
        // Edge case: empty token and secret
        Token token2 = new Token("", "");
        assertEquals("Token[ , ]", token2.toString());
    }
}