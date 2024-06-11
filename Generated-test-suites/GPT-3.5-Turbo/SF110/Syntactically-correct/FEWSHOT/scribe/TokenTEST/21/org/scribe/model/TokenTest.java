package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import org.junit.Test;

public class TokenTest {
    @Test
    public void testConstructorWithTokenAndSecret() {
        Token token = new Token("abc", "123");
        assertEquals("abc", token.getToken());
        assertEquals("123", token.getSecret());
        assertNull(token.getRawResponse());
    }
    
    @Test
    public void testConstructorWithTokenSecretAndRawResponse() {
        Token token = new Token("abc", "123", "raw");
        assertEquals("abc", token.getToken());
        assertEquals("123", token.getSecret());
        assertEquals("raw", token.getRawResponse());
    }
    
    @Test
    public void testGetToken() {
        Token token = new Token("abc", "123");
        assertEquals("abc", token.getToken());
    }
    
    @Test
    public void testGetSecret() {
        Token token = new Token("abc", "123");
        assertEquals("123", token.getSecret());
    }
    
    @Test
    public void testGetRawResponse() {
        Token token = new Token("abc", "123", "raw");
        assertEquals("raw", token.getRawResponse());
    }
    
    @Test
    public void testGetRawResponseWithNull() {
        Token token = new Token("abc", "123");
        assertThrows(IllegalStateException.class, () -> {
            token.getRawResponse();
        });
    }
    
    @Test
    public void testToString() {
        Token token = new Token("abc", "123");
        assertEquals("Token[abc , 123]", token.toString());
    }
    
}