
package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenTest {
    @Test
    public void testConstructorWithTwoArguments() {
        String token = "token";
        String secret = "secret";
        Token t = new Token(token, secret);

        assertEquals(token, t.getToken());
        assertEquals(secret, t.getSecret());
        assertNull(t.getRawResponse());
        assertEquals("Token[token , secret]", t.toString());
    }

    @Test
    public void testConstructorWithThreeArguments() {
        String token = "token";
        String secret = "secret";
        String rawResponse = "response";
        Token t = new Token(token, secret, rawResponse);

        assertEquals(token, t.getToken());
        assertEquals(secret, t.getSecret());
        assertEquals(rawResponse, t.getRawResponse());
        assertEquals("Token[token , secret]", t.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseThrowException() {
        Token t = new Token("token", "secret");
        t.getRawResponse();
    }
}
