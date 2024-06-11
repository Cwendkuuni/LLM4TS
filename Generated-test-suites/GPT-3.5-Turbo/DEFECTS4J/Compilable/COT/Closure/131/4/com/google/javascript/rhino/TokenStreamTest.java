package com.google.javascript.rhino;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("in"));
        assertTrue(TokenStream.isKeyword("void"));
        assertFalse(TokenStream.isKeyword("foo"));
        assertFalse(TokenStream.isKeyword("123"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("abc"));
        assertTrue(TokenStream.isJSIdentifier("_abc"));
        assertTrue(TokenStream.isJSIdentifier("$abc"));
        assertFalse(TokenStream.isJSIdentifier("123"));
        assertFalse(TokenStream.isJSIdentifier("a b c"));
    }

    // Add additional test cases for other public methods if any
}

