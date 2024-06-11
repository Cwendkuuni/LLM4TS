package com.google.javascript.rhino;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenStreamTest {
    
    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("while"));
        assertFalse(TokenStream.isKeyword("test"));
        assertTrue(TokenStream.isKeyword("switch"));
        assertFalse(TokenStream.isKeyword("hello"));
    }
    
    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("abc"));
        assertFalse(TokenStream.isJSIdentifier("123"));
        assertTrue(TokenStream.isJSIdentifier("helloWorld"));
        assertFalse(TokenStream.isJSIdentifier("hello world"));
    }
}