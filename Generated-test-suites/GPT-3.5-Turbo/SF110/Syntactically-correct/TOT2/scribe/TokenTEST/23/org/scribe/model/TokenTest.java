package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testConstructor() {
        Token token = new Token("tokenValue", "secretValue");
        assertEquals("tokenValue", token.getToken());
        assertEquals("secretValue", token.getSecret());
        assertNull(token.getRawResponse());
    }

    @Test
    public void testConstructorWithRawResponse() {
        Token token = new Token("tokenValue", "secretValue", "rawResponseValue");
        assertEquals("tokenValue", token.getToken());
        assertEquals("secretValue", token.getSecret());
        assertEquals("rawResponseValue", token.getRawResponse());
    }

    @Test
    public void testGetToken() {
        Token token = new Token("tokenValue", "secretValue");
        assertEquals("tokenValue", token.getToken());
    }

    @Test
    public void testGetSecret() {
        Token token = new Token("tokenValue", "secretValue");
        assertEquals("secretValue", token.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        Token token = new Token("tokenValue", "secretValue", "rawResponseValue");
        assertEquals("rawResponseValue", token.getRawResponse());
    }

    @Test
    public void testGetRawResponseException() {
        Token token = new Token("tokenValue", "secretValue");
        try {
            token.getRawResponse();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Exception is expected
        }
    }

    @Test
    public void testToString() {
        Token token = new Token("tokenValue", "secretValue", "rawResponseValue");
        assertEquals("Token[tokenValue , secretValue]", token.toString());
    }

}