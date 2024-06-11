package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenConstructor() {
        Token token = new Token("token", "secret");
        assertEquals("token", token.getToken());
        assertEquals("secret", token.getSecret());
        assertNull(token.getRawResponse());
        
        Token tokenWithResponse = new Token("token", "secret", "rawResponse");
        assertEquals("token", tokenWithResponse.getToken());
        assertEquals("secret", tokenWithResponse.getSecret());
        assertEquals("rawResponse", tokenWithResponse.getRawResponse());
    }
    
    @Test
    public void testGetToken() {
        Token token = new Token("token", "secret");
        assertEquals("token", token.getToken());
    }
    
    @Test
    public void testGetSecret() {
        Token token = new Token("token", "secret");
        assertEquals("secret", token.getSecret());
    }
    
    @Test
    public void testGetRawResponse() {
        Token token = new Token("token", "secret", "rawResponse");
        assertEquals("rawResponse", token.getRawResponse());
    }
    
    @Test
    public void testGetRawResponseThrowsException() {
        Token token = new Token("token", "secret");
        assertThrows(IllegalStateException.class, token::getRawResponse);
    }
    
    @Test
    public void testToString() {
        Token token = new Token("token", "secret");
        assertEquals("Token[token , secret]", token.toString());
    }
}