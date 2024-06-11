package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    
    @Test
    public void testTokenConstructor() {
        String token = "token";
        String secret = "secret";
        Token t = new Token(token, secret);
        assertEquals(token, t.getToken());
        assertEquals(secret, t.getSecret());
        assertNull(t.getRawResponse());
    }
    
    @Test
    public void testTokenConstructorWithRawResponse() {
        String token = "token";
        String secret = "secret";
        String rawResponse = "raw";
        Token t = new Token(token, secret, rawResponse);
        assertEquals(token, t.getToken());
        assertEquals(secret, t.getSecret());
        assertEquals(rawResponse, t.getRawResponse());
    }
    
    @Test
    public void testGetToken() {
        String token = "token";
        String secret = "secret";
        Token t = new Token(token, secret);
        assertEquals(token, t.getToken());
    }
    
    @Test
    public void testGetSecret() {
        String token = "token";
        String secret = "secret";
        Token t = new Token(token, secret);
        assertEquals(secret, t.getSecret());
    }
    
    @Test
    public void testGetRawResponseValid() {
        String token = "token";
        String secret = "secret";
        String rawResponse = "raw";
        Token t = new Token(token, secret, rawResponse);
        assertEquals(rawResponse, t.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseInvalid() {
        String token = "token";
        String secret = "secret";
        Token t = new Token(token, secret);
        t.getRawResponse();
    }
    
    @Test
    public void testToString() {
        String token = "token";
        String secret = "secret";
        Token t = new Token(token, secret);
        String expected = String.format("Token[%s , %s]", token, secret);
        assertEquals(expected, t.toString());
    }
}