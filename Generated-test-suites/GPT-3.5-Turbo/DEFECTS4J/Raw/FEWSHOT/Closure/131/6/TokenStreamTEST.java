package com.google.javascript.rhino;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {
    
    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("break"));
        assertTrue(TokenStream.isKeyword("void"));
        assertTrue(TokenStream.isKeyword("continue"));
        assertTrue(TokenStream.isKeyword("default"));
        assertTrue(TokenStream.isKeyword("new"));
        
        assertFalse(TokenStream.isKeyword("abc"));
        assertFalse(TokenStream.isKeyword("123"));
        assertFalse(TokenStream.isKeyword("true"));
        assertFalse(TokenStream.isKeyword("false"));
        assertFalse(TokenStream.isKeyword("test"));
    }
    
    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("foo"));
        assertTrue(TokenStream.isJSIdentifier("bar"));
        assertTrue(TokenStream.isJSIdentifier("_test"));
        assertTrue(TokenStream.isJSIdentifier("$123"));
        
        assertFalse(TokenStream.isJSIdentifier("123"));
        assertFalse(TokenStream.isJSIdentifier("if"));
        assertFalse(TokenStream.isJSIdentifier("break"));
        assertFalse(TokenStream.isJSIdentifier("void"));
        assertFalse(TokenStream.isJSIdentifier("continue"));
    }
}