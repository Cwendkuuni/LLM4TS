package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testGetToken() {
        Token token = new Token("testToken", "testSecret");
        assertEquals("testToken", token.getToken());
    }

    @Test
    public void testGetSecret() {
        Token token = new Token("testToken", "testSecret");
        assertEquals("testSecret", token.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        Token token = new Token("testToken", "testSecret", "testRawResponse");
        assertEquals("testRawResponse", token.getRawResponse());
    }

    @Test
    public void testToString() {
        Token token = new Token("testToken", "testSecret", "testRawResponse");
        assertEquals("Token[testToken , testSecret]", token.toString());
    }
    
    @Test
    public void testGetRawResponseNullCheck() {
        Token token = new Token("testToken", "testSecret");
        try {
            token.getRawResponse();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("This token object was not constructed by scribe and does not have a rawResponse", e.getMessage());
        }
    }
}