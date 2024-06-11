package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {
    
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
    
    @Test
    public void testGetRawResponse() {
        Token token = new Token("myToken", "mySecret", "rawResponse");
        assertEquals("rawResponse", token.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseException() {
        Token token = new Token("myToken", "mySecret");
        token.getRawResponse();
    }
    
    @Test
    public void testToString() {
        Token token = new Token("myToken", "mySecret");
        assertEquals("Token[myToken , mySecret]", token.toString());
    }
}