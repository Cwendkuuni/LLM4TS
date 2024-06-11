package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testGetToken() {
        Token token = new Token("123", "456");
        assertEquals("123", token.getToken());
    }

    @Test
    public void testGetSecret() {
        Token token = new Token("123", "456");
        assertEquals("456", token.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        Token token = new Token("123", "456", "raw");
        assertEquals("raw", token.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseException() {
        Token token = new Token("123", "456");
        token.getRawResponse();
    }

    @Test
    public void testToString() {
        Token token = new Token("123", "456");
        assertEquals("Token[123 , 456]", token.toString());
    }
}