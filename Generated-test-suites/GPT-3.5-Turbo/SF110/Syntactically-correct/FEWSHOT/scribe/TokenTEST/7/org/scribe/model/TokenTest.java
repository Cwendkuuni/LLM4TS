package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenTest {
    @Test
    public void testConstructor1() {
        Token token = new Token("access_token", "access_secret");
        assertEquals("access_token", token.getToken());
        assertEquals("access_secret", token.getSecret());
        assertNull(token.getRawResponse());
    }
    
    @Test
    public void testConstructor2() {
        Token token = new Token("access_token", "access_secret", "raw_response");
        assertEquals("access_token", token.getToken());
        assertEquals("access_secret", token.getSecret());
        assertEquals("raw_response", token.getRawResponse());
    }
    
    @Test
    public void testToString() {
        Token token = new Token("access_token", "access_secret");
        assertEquals("Token[access_token , access_secret]", token.toString());
    }
    
    @Test
    public void testGetRawResponse() {
        Token token = new Token("access_token", "access_secret");
        try {
            token.getRawResponse();
        } catch (IllegalStateException e) {
            assertEquals("This token object was not constructed by scribe and does not have a rawResponse", e.getMessage());
        }
    }
}