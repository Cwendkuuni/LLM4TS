package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenTest {
    @Test
    public void testConstructor() {
        Token token = new Token("access_token", "access_secret");
        assertNotNull(token);
        assertEquals("access_token", token.getToken());
        assertEquals("access_secret", token.getSecret());
        assertNull(token.getRawResponse());
        
        token = new Token("access_token", "access_secret", "raw_response");
        assertNotNull(token);
        assertEquals("access_token", token.getToken());
        assertEquals("access_secret", token.getSecret());
        assertEquals("raw_response", token.getRawResponse());
    }
    
    @Test
    public void testGetToken() {
        Token token = new Token("access_token", "access_secret");
        assertEquals("access_token", token.getToken());
    }
    
    @Test
    public void testGetSecret() {
        Token token = new Token("access_token", "access_secret");
        assertEquals("access_secret", token.getSecret());
    }
    
    @Test
    public void testGetRawResponse() {
        Token token = new Token("access_token", "access_secret", "raw_response");
        assertEquals("raw_response", token.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponse() {
        Token token = new Token("access_token", "access_secret");
        token.getRawResponse();
    }
    
    @Test
    public void testToString() {
        Token token = new Token("access_token", "access_secret");
        assertEquals("Token[access_token , access_secret]", token.toString());
    }
}