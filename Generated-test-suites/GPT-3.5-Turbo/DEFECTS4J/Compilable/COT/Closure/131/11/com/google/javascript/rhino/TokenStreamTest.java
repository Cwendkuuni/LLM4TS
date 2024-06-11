package com.google.javascript.rhino;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Test basic functionality
        assertTrue(TokenStream.isKeyword("if"));
        assertFalse(TokenStream.isKeyword("foo"));

        // Test edge cases
        assertFalse(TokenStream.isKeyword(""));
        assertFalse(TokenStream.isKeyword("i"));
    }

    @Test
    public void testIsJSIdentifier() {
        // Test basic functionality
        assertTrue(TokenStream.isJSIdentifier("testVariable"));
        assertFalse(TokenStream.isJSIdentifier("123"));

        // Test edge cases
        assertFalse(TokenStream.isJSIdentifier(""));
        assertFalse(TokenStream.isJSIdentifier(" "));
    }

}