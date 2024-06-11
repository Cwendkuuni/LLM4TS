package com.google.javascript.rhino;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Basic test case
        assertTrue(TokenStream.isKeyword("if"));
        
        // Edge cases - 1 letter keywords
        assertTrue(TokenStream.isKeyword("a"));
        assertTrue(TokenStream.isKeyword("i"));
        assertTrue(TokenStream.isKeyword("f"));
        
        // Edge cases - 4 letter keywords
        assertTrue(TokenStream.isKeyword("void"));
        assertTrue(TokenStream.isKeyword("long"));
        assertTrue(TokenStream.isKeyword("null"));
        
        // Edge case - null value
        assertFalse(TokenStream.isKeyword(null));
    }

    @Test
    public void testIsJSIdentifier() {
        // Basic test case
        assertTrue(TokenStream.isJSIdentifier("test123"));
        
        // Edge cases - empty string
        assertFalse(TokenStream.isJSIdentifier(""));
        
        // Edge cases - special characters
        assertFalse(TokenStream.isJSIdentifier("@test"));
        assertFalse(TokenStream.isJSIdentifier("test-123"));
        
        // Exception handling - invalid characters
        assertFalse(TokenStream.isJSIdentifier("123test"));
        assertFalse(TokenStream.isJSIdentifier("test 123"));
    }
}