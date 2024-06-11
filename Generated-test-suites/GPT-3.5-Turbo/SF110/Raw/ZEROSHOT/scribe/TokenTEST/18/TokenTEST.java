package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testGetToken() {
        Token token = new Token("token", "secret");
        assertEquals("token", token.getToken());
    }
    
    @Test
    public void testGetSecret() {
        Token token = new Token("token", "secret");
        assertEquals("secret", token.getSecret());
    }
    
    @Test
    public void testGetRawResponseWithoutRawResponse() {
        Token token = new Token("token", "secret");
        try {
            token.getRawResponse();
        } catch (IllegalStateException e) {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testGetRawResponseWithRawResponse() {
        Token token = new Token("token", "secret", "rawResponse");
        assertEquals("rawResponse", token.getRawResponse());
    }
    
    @Test
    public void testToString() {
        Token token = new Token("token", "secret");
        assertEquals("Token[token , secret]", token.toString());
    }
}