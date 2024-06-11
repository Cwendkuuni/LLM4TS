package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenConstructor() {
        Token token = new Token("dummyToken", "dummySecret");
        assertEquals("dummyToken", token.getToken());
        assertEquals("dummySecret", token.getSecret());
    }

    @Test
    public void testTokenWithRawResponseConstructor() {
        Token token = new Token("dummyToken", "dummySecret", "dummyRawResponse");
        assertEquals("dummyToken", token.getToken());
        assertEquals("dummySecret", token.getSecret());
        assertEquals("dummyRawResponse", token.getRawResponse());
    }

    @Test
    public void testGetToken() {
        Token token = new Token("dummyToken", "dummySecret");
        assertEquals("dummyToken", token.getToken());
    }

    @Test
    public void testGetSecret() {
        Token token = new Token("dummyToken", "dummySecret");
        assertEquals("dummySecret", token.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        Token token = new Token("dummyToken", "dummySecret", "dummyRawResponse");
        assertEquals("dummyRawResponse", token.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseException() {
        Token token = new Token("dummyToken", "dummySecret");
        token.getRawResponse();
    }

    @Test
    public void testToString() {
        Token token = new Token("dummyToken", "dummySecret");
        assertEquals("Token[dummyToken , dummySecret]", token.toString());
    }
}