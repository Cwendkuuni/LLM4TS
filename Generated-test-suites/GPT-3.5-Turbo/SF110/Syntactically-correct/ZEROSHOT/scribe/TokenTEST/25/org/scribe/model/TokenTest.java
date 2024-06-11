package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.*;

public class TokenTest {
    // Test getToken() method
    @Test
    public void testGetToken() {
        Token token = new Token("abc123", "secret123");
        assertEquals("abc123", token.getToken());
    }

    // Test getSecret() method
    @Test
    public void testGetSecret() {
        Token token = new Token("abc123", "secret123");
        assertEquals("secret123", token.getSecret());
    }

    // Test getRawResponse() method when rawResponse is null
    @Test(expected=IllegalStateException.class)
    public void testGetRawResponseNull() {
        Token token = new Token("abc123", "secret123");
        token.getRawResponse();
    }

    // Test getRawResponse() method when rawResponse is not null
    @Test
    public void testGetRawResponse() {
        Token token = new Token("abc123", "secret123", "response");
        assertEquals("response", token.getRawResponse());
    }

    // Test toString() method
    @Test
    public void testToString() {
        Token token = new Token("abc123", "secret123");
        assertEquals("Token[abc123 , secret123]", token.toString());
    }
}