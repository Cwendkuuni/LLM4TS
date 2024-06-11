package org.scribe.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;

public class TokenTest {

    private Token token;

    @Before
    public void setUp() {
        token = new Token("sampleToken", "sampleSecret", "sampleRawResponse");
    }

    @Test
    public void testGetToken() {
        assertEquals("sampleToken", token.getToken());
    }

    @Test
    public void testGetSecret() {
        assertEquals("sampleSecret", token.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        assertEquals("sampleRawResponse", token.getRawResponse());
    }

    @Test
    public void testGetRawResponse_Exception() {
        Token invalidToken = new Token("invalidToken", "invalidSecret");
        try {
            invalidToken.getRawResponse();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("This token object was not constructed by scribe and does not have a rawResponse",
                    e.getMessage());
        }
    }

    @Test
    public void testToString() {
        String expected = "Token[sampleToken , sampleSecret]";
        assertEquals(expected, token.toString());
    }
}