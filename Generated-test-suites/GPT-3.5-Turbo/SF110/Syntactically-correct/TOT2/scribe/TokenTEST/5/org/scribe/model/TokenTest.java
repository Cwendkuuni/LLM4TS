package org.scribe.model;
import org.junit.*;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testConstructor1() {
        Token token = new Token("access_token", "secret_token");
        assertEquals("access_token", token.getToken());
        assertEquals("secret_token", token.getSecret());
        assertNull(token.getRawResponse());
        assertEquals("Token[access_token , secret_token]", token.toString());
    }

    @Test
    public void testConstructor2() {
        Token token = new Token("access_token", "secret_token", "raw_response");
        assertEquals("access_token", token.getToken());
        assertEquals("secret_token", token.getSecret());
        assertEquals("raw_response", token.getRawResponse());
        assertEquals("Token[access_token , secret_token]", token.toString());
    }

    @Test
    public void testGetRawResponse_exception() {
        Token token = new Token("access_token", "secret_token");
        try {
            token.getRawResponse();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("This token object was not constructed by scribe"));
        }
    }
}