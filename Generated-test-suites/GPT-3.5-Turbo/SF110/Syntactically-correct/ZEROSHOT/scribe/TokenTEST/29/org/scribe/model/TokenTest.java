package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testGetToken() {
        Token token = new Token("abc123", "def456");
        assertEquals("abc123", token.getToken());
    }

    @Test
    public void testGetSecret() {
        Token token = new Token("abc123", "def456");
        assertEquals("def456", token.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        Token token = new Token("abc123", "def456", "raw");
        assertEquals("raw", token.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponse() {
        Token token = new Token("abc123", "def456");
        token.getRawResponse();
    }

    @Test
    public void testToString() {
        Token token = new Token("abc123", "def456");
        assertEquals("Token[abc123 , def456]", token.toString());
    }

}