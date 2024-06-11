package com.google.javascript.rhino;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {
    
    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertFalse(TokenStream.isKeyword("abc"));
        assertTrue(TokenStream.isKeyword("byte"));
        assertTrue(TokenStream.isKeyword("boolean"));
        assertFalse(TokenStream.isKeyword("xyz"));
    }
    
    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("validIdentifier"));
        assertFalse(TokenStream.isJSIdentifier("123Invalid"));
        assertFalse(TokenStream.isJSIdentifier(""));
        assertTrue(TokenStream.isJSIdentifier("camelCase"));
    }
    
    // Additional test cases for edge cases and exceptions can be added here
    
}