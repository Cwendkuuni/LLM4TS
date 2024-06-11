package org.scribe.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    private Token token;

    @Before
    public void setup() {
        token = new Token("testToken", "testSecret", "testRawResponse");
    }

    @Test
    public void testGetToken() {
        assertEquals("testToken", token.getToken());
    }

    @Test
    public void testGetSecret() {
        assertEquals("testSecret", token.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        assertEquals("testRawResponse", token.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseThrowsException() {
        Token emptyToken = new Token("emptyToken", "emptySecret");
        emptyToken.getRawResponse();
    }

    @Test
    public void testToString() {
        assertEquals("Token[testToken , testSecret]", token.toString());
    }

    // Additional tests can be added for edge cases or custom scenarios

}