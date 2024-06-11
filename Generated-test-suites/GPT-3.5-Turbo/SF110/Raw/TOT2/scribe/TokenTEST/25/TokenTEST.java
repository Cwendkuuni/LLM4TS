package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {
    
    @Test
    public void testConstructor() {
        Token token = new Token("token", "secret");
        assertEquals("token", token.getToken());
        assertEquals("secret", token.getSecret());
    }
    
    @Test
    public void testConstructorWithRawResponse() {
        Token token = new Token("token", "secret", "rawResponse");
        assertEquals("token", token.getToken());
        assertEquals("secret", token.getSecret());
        assertEquals("rawResponse", token.getRawResponse());
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
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithNullRawResponse() {
        Token token = new Token("token", "secret");
        token.getRawResponse();
    }
    
    @Test
    public void testToString() {
        Token token = new Token("token", "secret");
        assertEquals("Token[token , secret]", token.toString());
    }
}