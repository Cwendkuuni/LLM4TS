package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class TokenTest {
    
    @Test
    public void testTokenConstructor() {
        Token token = new Token("abc123", "xyz789");
        
        assertNotNull(token);
        assertEquals("abc123", token.getToken());
        assertEquals("xyz789", token.getSecret());
        assertNull(token.getRawResponse());
    }
    
    @Test
    public void testTokenConstructorWithRawResponse() {
        Token token = new Token("abc123", "xyz789", "raw123");
        
        assertNotNull(token);
        assertEquals("abc123", token.getToken());
        assertEquals("xyz789", token.getSecret());
        assertEquals("raw123", token.getRawResponse());
    }
    
    @Test
    public void testGetToken() {
        Token token = new Token("abc123", "xyz789");
        
        assertEquals("abc123", token.getToken());
    }
    
    @Test
    public void testGetSecret() {
        Token token = new Token("abc123", "xyz789");
        
        assertEquals("xyz789", token.getSecret());
    }
    
    @Test
    public void testGetRawResponse() {
        Token token = new Token("abc123", "xyz789", "raw123");
        
        assertEquals("raw123", token.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponse() {
        Token token = new Token("abc123", "xyz789");
        
        token.getRawResponse();
    }
    
    @Test
    public void testToString() {
        Token token = new Token("abc123", "xyz789");
        
        String expected = "Token[abc123 , xyz789]";
        
        assertEquals(expected, token.toString());
    }    
}