package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenConstructor() {
        Token token = new Token("sampleToken", "sampleSecret");
        assertNotNull(token);
    }

    @Test
    public void testGetToken() {
        Token token = new Token("sampleToken", "sampleSecret");
        assertEquals("sampleToken", token.getToken());
    }

    @Test
    public void testGetSecret() {
        Token token = new Token("sampleToken", "sampleSecret");
        assertEquals("sampleSecret", token.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        Token token = new Token("sampleToken", "sampleSecret", "sampleResponse");
        assertEquals("sampleResponse", token.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponse() {
        Token token = new Token("sampleToken", "sampleSecret");
        token.getRawResponse();
    }

    @Test
    public void testToString() {
        Token token = new Token("sampleToken", "sampleSecret");
        assertEquals("Token[sampleToken , sampleSecret]", token.toString());
    }
}