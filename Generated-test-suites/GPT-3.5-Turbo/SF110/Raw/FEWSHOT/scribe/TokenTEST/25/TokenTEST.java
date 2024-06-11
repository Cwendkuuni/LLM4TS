package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokenTest {
    @Test
    public void testConstructor() {
        Token token = new Token("testToken", "testSecret");
        
        assertNotNull(token);
        assertEquals("testToken", token.getToken());
        assertEquals("testSecret", token.getSecret());
        assertNull(token.getRawResponse());
    }
    
    @Test
    public void testConstructorWithRawResponse() {
        Token token = new Token("testToken", "testSecret", "testResponse");
        
        assertNotNull(token);
        assertEquals("testToken", token.getToken());
        assertEquals("testSecret", token.getSecret());
        assertEquals("testResponse", token.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseException() {
        Token token = new Token("testToken", "testSecret");
        token.getRawResponse();
    }
    
    @Test
    public void testToString() {
        Token token = new Token("testToken", "testSecret");
        
        assertEquals("Token[testToken , testSecret]", token.toString());
    }
}