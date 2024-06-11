package org.scribe.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TokenTest {
    @Test
    public void testGetToken() {
        Token token = new Token("123", "abc");
        assertEquals("123", token.getToken());
    }
    
    @Test
    public void testGetSecret() {
        Token token = new Token("123", "abc");
        assertEquals("abc", token.getSecret());
    }
    
    @Test
    public void testGetRawResponse() {
        Token token = new Token("123", "abc", "raw");
        assertEquals("raw", token.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseIllegalState() {
        Token token = new Token("123", "abc");
        token.getRawResponse();
    }
    
    @Test
    public void testToString() {
        Token token = new Token("123", "abc");
        assertEquals("Token[123 , abc]", token.toString());
    }
}