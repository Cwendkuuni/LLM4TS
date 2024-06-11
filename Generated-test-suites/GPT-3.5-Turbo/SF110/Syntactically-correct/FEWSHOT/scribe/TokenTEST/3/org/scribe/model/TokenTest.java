package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class TokenTest {
    @Test
    public void testConstructorWithoutRawResponse() {
        Token token = new Token("token", "secret");
        assertNotNull(token);
        assertEquals("token", token.getToken());
        assertEquals("secret", token.getSecret());
        assertNull(token.getRawResponse());
        assertEquals("Token[token , secret]", token.toString());
    }
    
    @Test
    public void testConstructorWithRawResponse() {
        Token token = new Token("token", "secret", "rawResponse");
        assertNotNull(token);
        assertEquals("token", token.getToken());
        assertEquals("secret", token.getSecret());
        assertEquals("rawResponse", token.getRawResponse());
        assertEquals("Token[token , secret]", token.toString());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponse() {
        Token token = new Token("token", "secret");
        token.getRawResponse();
    }
}