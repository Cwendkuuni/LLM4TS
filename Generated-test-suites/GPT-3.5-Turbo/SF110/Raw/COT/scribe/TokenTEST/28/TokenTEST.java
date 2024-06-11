
package org.scribe.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    private Token token;

    @Before
    public void setUp() {
        token = new Token("testToken", "testSecret");
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
        assertNull(token.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseException() {
        Token incompleteToken = new Token("incompleteToken", "incompleteSecret");
        incompleteToken.getRawResponse();
    }

    @Test
    public void testToString() {
        assertEquals("Token[testToken , testSecret]", token.toString());
    }
}
