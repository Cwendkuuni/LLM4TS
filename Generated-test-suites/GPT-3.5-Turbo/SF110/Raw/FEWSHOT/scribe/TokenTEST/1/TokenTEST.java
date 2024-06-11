package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testConstructor() {
        Token token = new Token("token", "secret");
        assertNotNull(token);
        assertEquals("token", token.getToken());
        assertEquals("secret", token.getSecret());
        assertNull(token.getRawResponse());

        Token tokenWithRawResponse = new Token("token", "secret", "rawResponse");
        assertNotNull(tokenWithRawResponse);
        assertEquals("token", tokenWithRawResponse.getToken());
        assertEquals("secret", tokenWithRawResponse.getSecret());
        assertEquals("rawResponse", tokenWithRawResponse.getRawResponse());
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