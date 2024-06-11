
package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenTest {
    
    @Test
    public void testConstructorWithTwoArguments() {
        String token = "abc";
        String secret = "123";
        Token t = new Token(token, secret);
        assertEquals(token, t.getToken());
        assertEquals(secret, t.getSecret());
        assertNull(t.getRawResponse());
    }
    
    @Test
    public void testConstructorWithThreeArguments() {
        String token = "abc";
        String secret = "123";
        String rawResponse = "some raw response";
        Token t = new Token(token, secret, rawResponse);
        assertEquals(token, t.getToken());
        assertEquals(secret, t.getSecret());
        assertEquals(rawResponse, t.getRawResponse());
    }
    
    @Test
    public void testGetToken() {
        String token = "abc";
        String secret = "123";
        Token t = new Token(token, secret);
        assertEquals(token, t.getToken());
    }
    
    @Test
    public void testGetSecret() {
        String token = "abc";
        String secret = "123";
        Token t = new Token(token, secret);
        assertEquals(secret, t.getSecret());
    }
    
    @Test
    public void testGetRawResponse() {
        String token = "abc";
        String secret = "123";
        String rawResponse = "some raw response";
        Token t = new Token(token, secret, rawResponse);
        assertEquals(rawResponse, t.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponse() {
        String token = "abc";
        String secret = "123";
        Token t = new Token(token, secret);
        t.getRawResponse();
    }
    
    @Test
    public void testToString() {
        String token = "abc";
        String secret = "123";
        Token t = new Token(token, secret);
        String expected = "Token[abc , 123]";
        assertEquals(expected, t.toString());
    }
}
