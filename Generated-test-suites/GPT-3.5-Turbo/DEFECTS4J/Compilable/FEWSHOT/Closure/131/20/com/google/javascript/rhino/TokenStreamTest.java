package com.google.javascript.rhino;
import static org.junit.Assert.assertEquals;


import org.junit.Test;

public class TokenStreamTest {
    
    @Test
    public void testIsKeyword() {
        assertEquals(true, TokenStream.isKeyword("if"));
        assertEquals(true, TokenStream.isKeyword("in"));
        assertEquals(true, TokenStream.isKeyword("goto"));
        assertEquals(false, TokenStream.isKeyword("test"));
        assertEquals(false, TokenStream.isKeyword(""));
        assertEquals(false, TokenStream.isKeyword(null));
    }

    @Test
    public void testIsJSIdentifier() {
        assertEquals(true, TokenStream.isJSIdentifier("test"));
        assertEquals(true, TokenStream.isJSIdentifier("abc123"));
        assertEquals(false, TokenStream.isJSIdentifier("123abc"));
        assertEquals(false, TokenStream.isJSIdentifier(""));
        assertEquals(false, TokenStream.isJSIdentifier(null));
    }
}