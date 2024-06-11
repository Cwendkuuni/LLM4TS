package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testConstructor() {
        Token token = new Token("token123", "secret123");
        assertEquals("token123", token.getToken());
        assertEquals("secret123", token.getSecret());
        assertNull(token.getRawResponse());
    }

    @Test
    public void testConstructorWithRawResponse() {
        Token token = new Token("token456", "secret456", "raw123");
        assertEquals("token456", token.getToken());
        assertEquals("secret456", token.getSecret());
        assertEquals("raw123", token.getRawResponse());
    }

    @Test
    public void testGetToken() {
        Token token = new Token("token123", "secret123");
        assertEquals("token123", token.getToken());
    }

    @Test
    public void testGetSecret() {
        Token token = new Token("token123", "secret123");
        assertEquals("secret123", token.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        Token token = new Token("token123", "secret123", "raw123");
        assertEquals("raw123", token.getRawResponse());
    }

    @Test
    public void testGetRawResponseWithNull() {
        Token token = new Token("token123", "secret123");
        try {
            token.getRawResponse();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("This token object was not constructed by scribe and does not have a rawResponse", e.getMessage());
        }
    }

    @Test
    public void testToString() {
        Token token = new Token("token123", "secret123");
        assertEquals("Token[token123 , secret123]", token.toString());
    }
}