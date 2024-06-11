package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {
    @Test
    public void testConstructorWithTwoArgs() {
        Token token = new Token("myToken", "mySecret");
        assertEquals("myToken", token.getToken());
        assertEquals("mySecret", token.getSecret());
        assertNull(token.getRawResponse());
    }
    
    @Test
    public void testConstructorWithThreeArgs() {
        Token token = new Token("myToken", "mySecret", "rawResponse");
        assertEquals("myToken", token.getToken());
        assertEquals("mySecret", token.getSecret());
        assertEquals("rawResponse", token.getRawResponse());
    }
    
    @Test
    public void testGetToken() {
        Token token = new Token("myToken", "mySecret");
        assertEquals("myToken", token.getToken());
    }
    
    @Test
    public void testGetSecret() {
        Token token = new Token("myToken", "mySecret");
        assertEquals("mySecret", token.getSecret());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponse_exception() {
        Token token = new Token("myToken", "mySecret");
        token.getRawResponse();
    }
    
    @Test
    public void testGetRawResponse() {
        Token token = new Token("myToken", "mySecret", "rawResponse");
        assertEquals("rawResponse", token.getRawResponse());
    }
    
    @Test
    public void testToString() {
        Token token = new Token("myToken", "mySecret");
        assertEquals("Token[myToken , mySecret]", token.toString());
    }
}