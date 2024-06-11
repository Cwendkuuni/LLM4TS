package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.*;

import org.junit.Test;

public class TokenTest {
    @Test
    public void testConstructorWithTwoArguments() {
        Token token = new Token("access_token", "access_secret");
        
        assertNotNull(token);
        assertEquals("access_token", token.getToken());
        assertEquals("access_secret", token.getSecret());
        assertEquals(null, token.getRawResponse());
    }
    
    @Test
    public void testConstructorWithThreeArguments() {
        Token token = new Token("access_token", "access_secret", "raw_response");
        
        assertNotNull(token);
        assertEquals("access_token", token.getToken());
        assertEquals("access_secret", token.getSecret());
        assertEquals("raw_response", token.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithNullRawResponse() {
        Token token = new Token("access_token", "access_secret");
        
        token.getRawResponse();
    }
    
    @Test
    public void testToString() {
        Token token = new Token("access_token", "access_secret");
        
        assertEquals("Token[access_token , access_secret]", token.toString());
    }
}