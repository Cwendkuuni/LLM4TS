package com.google.javascript.rhino;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {
    
    @Test
    public void testIsKeyword() {
        // Typical use cases
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("do"));
        
        // Edge cases
        assertFalse(TokenStream.isKeyword("foo"));  // not a keyword
        assertFalse(TokenStream.isKeyword(""));     // empty string
        assertFalse(TokenStream.isKeyword("i"));    // single character
    }
    
    @Test
    public void testIsJSIdentifier() {
        // Typical use cases
        assertTrue(TokenStream.isJSIdentifier("variable"));
        assertTrue(TokenStream.isJSIdentifier("anotherVariable"));
        assertTrue(TokenStream.isJSIdentifier("an_identifier"));

        // Edge cases
        assertFalse(TokenStream.isJSIdentifier("123"));    // starts with a number
        assertFalse(TokenStream.isJSIdentifier(""));       // empty string
        assertFalse(TokenStream.isJSIdentifier("_private")); // starts with underscore
        assertFalse(TokenStream.isJSIdentifier("a variable")); // contains a space
    }
}