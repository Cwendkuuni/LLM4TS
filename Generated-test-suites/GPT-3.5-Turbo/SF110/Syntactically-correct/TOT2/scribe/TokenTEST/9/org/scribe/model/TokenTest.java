package org.scribe.model;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testConstructorWithTwoArguments() {
        Token token = new Token("access_token", "123456");
        assertEquals("access_token", token.getToken());
        assertEquals("123456", token.getSecret());
        assertNull(token.getRawResponse());
    }

    @Test
    public void testConstructorWithThreeArguments() {
        Token token = new Token("access_token", "123456", "response");
        assertEquals("access_token", token.getToken());
        assertEquals("123456", token.getSecret());
        assertEquals("response", token.getRawResponse());
    }

    @Test
    public void testGetToken() {
        Token token = new Token("access_token", "123456");
        assertEquals("access_token", token.getToken());
    }

    @Test
    public void testGetSecret() {
        Token token = new Token("access_token", "123456");
        assertEquals("123456", token.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        Token token = new Token("access_token", "123456", "response");
        assertEquals("response", token.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseException() {
        Token token = new Token("access_token", "123456");
        token.getRawResponse();
    }

    @Test
    public void testToString() {
        Token token = new Token("access_token", "123456");
        assertEquals("Token[access_token , 123456]", token.toString());
    }
}