package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    
    @Test
    public void testTokenConstructor() {
        Token token = new Token("token", "secret");
        assertEquals("token", token.getToken());
        assertEquals("secret", token.getSecret());
        assertNull(token.getRawResponse());
    }
    
    @Test
    public void testTokenConstructorWithRawResponse() {
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
    
    @Test
    public void testGetRawResponseException() {
        Token token = new Token("token", "secret");
        try {
            token.getRawResponse();
            fail("Expected IllegalStateException was not thrown");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testToString() {
        Token token = new Token("token", "secret");
        assertEquals("Token[token , secret]", token.toString());
    }
}