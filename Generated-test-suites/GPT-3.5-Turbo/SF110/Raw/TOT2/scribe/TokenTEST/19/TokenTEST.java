package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    // Test cases for constructor with two arguments
    @Test
    public void testConstructorWithTwoArguments() {
        Token token = new Token("dummyToken", "dummySecret");

        assertEquals("dummyToken", token.getToken());
        assertEquals("dummySecret", token.getSecret());
    }

    // Test cases for constructor with three arguments
    @Test
    public void testConstructorWithThreeArguments() {
        Token token = new Token("dummyToken", "dummySecret", "dummyResponse");

        assertEquals("dummyToken", token.getToken());
        assertEquals("dummySecret", token.getSecret());
        assertEquals("dummyResponse", token.getRawResponse());
    }

    // Test case for getRawResponse method with null raw response
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithNullResponse() {
        Token token = new Token("dummyToken", "dummySecret");

        token.getRawResponse();
    }

    // Test case for toString method
    @Test
    public void testToString() {
        Token token = new Token("dummyToken", "dummySecret");

        assertEquals("Token[dummyToken , dummySecret]", token.toString());
    }

}