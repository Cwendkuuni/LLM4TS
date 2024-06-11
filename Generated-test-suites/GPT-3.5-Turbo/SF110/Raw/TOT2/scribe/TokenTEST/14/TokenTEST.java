package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenTest {

    @Test
    public void testTokenConstructor() {
        Token token = new Token("tokenValue", "secretValue");
        assertEquals("tokenValue", token.getToken());
        assertEquals("secretValue", token.getSecret());
    }

    @Test
    public void testTokenWithRawResponseConstructor() {
        Token token = new Token("tokenValue", "secretValue", "rawResponseValue");
        assertEquals("tokenValue", token.getToken());
        assertEquals("secretValue", token.getSecret());
        assertEquals("rawResponseValue", token.getRawResponse());
    }

    @Test
    public void testGetRawResponseWithNullRawResponse() {
        Token token = new Token("tokenValue", "secretValue");
        try {
            token.getRawResponse();
        } catch (IllegalStateException e) {
            assertEquals("This token object was not constructed by scribe and does not have a rawResponse",
                    e.getMessage());
        }
    }

    @Test
    public void testToStringMethod() {
        Token token = new Token("tokenValue", "secretValue");
        String expected = "Token[tokenValue , secretValue]";
        assertEquals(expected, token.toString());
    }
}