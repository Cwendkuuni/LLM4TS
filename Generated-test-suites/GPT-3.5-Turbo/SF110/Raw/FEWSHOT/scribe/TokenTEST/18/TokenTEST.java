package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TokenTest {
    @Test
    public void testConstructor1() {
        Token token = new Token("token", "secret");
        assertEquals("token", token.getToken());
        assertEquals("secret", token.getSecret());
        assertNotNull(token.toString());
    }
    
    @Test
    public void testConstructor2() {
        Token token = new Token("token", "secret", "rawResponse");
        assertEquals("token", token.getToken());
        assertEquals("secret", token.getSecret());
        assertEquals("rawResponse", token.getRawResponse());
        assertNotNull(token.toString());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponse() {
        Token token = new Token("token", "secret");
        token.getRawResponse();
    }
}