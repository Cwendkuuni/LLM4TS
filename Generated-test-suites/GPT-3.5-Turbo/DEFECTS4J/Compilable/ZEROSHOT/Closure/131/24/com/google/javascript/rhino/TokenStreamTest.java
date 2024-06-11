package com.google.javascript.rhino;
import static org.junit.Assert.*;

import org.junit.Test;

public class TokenStreamTest {
    
    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("null"));
        assertFalse(TokenStream.isKeyword("abc"));
        assertFalse(TokenStream.isKeyword("123"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("abc"));
        assertTrue(TokenStream.isJSIdentifier("ABC"));
        assertTrue(TokenStream.isJSIdentifier("abc123"));
        assertFalse(TokenStream.isJSIdentifier("123abc"));
        assertFalse(TokenStream.isJSIdentifier(""));
        assertFalse(TokenStream.isJSIdentifier("_abc"));
    }
}