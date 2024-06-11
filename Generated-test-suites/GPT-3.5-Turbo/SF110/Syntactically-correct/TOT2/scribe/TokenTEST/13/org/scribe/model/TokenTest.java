package org.scribe.model;
import static org.junit.Assert.*;
import org.junit.Test;

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
        Token token = new Token("token", "secret", "raw");
        assertEquals("token", token.getToken());
        assertEquals("secret", token.getSecret());
        assertEquals("raw", token.getRawResponse());
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
        Token token = new Token("token", "secret", "raw");
        assertEquals("raw", token.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponse() {
        Token token = new Token("token", "secret");
        token.getRawResponse();
    }

    @Test
    public void testToString() {
        Token token = new Token("token", "secret");
        assertEquals("Token[token , secret]", token.toString());
    }
}