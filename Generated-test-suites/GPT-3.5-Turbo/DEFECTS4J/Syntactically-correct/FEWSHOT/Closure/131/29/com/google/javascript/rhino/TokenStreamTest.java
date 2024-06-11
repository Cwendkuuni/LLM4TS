package com.google.javascript.rhino;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenStreamTest {
    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("else"));
        assertTrue(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("break"));
        assertTrue(TokenStream.isKeyword("continue"));
        assertTrue(TokenStream.isKeyword("try"));
        assertTrue(TokenStream.isKeyword("catch"));
        assertTrue(TokenStream.isKeyword("finally"));
        assertTrue(TokenStream.isKeyword("do"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("switch"));
        assertTrue(TokenStream.isKeyword("case"));
        assertTrue(TokenStream.isKeyword("default"));
        assertTrue(TokenStream.isKeyword("function"));
        assertTrue(TokenStream.isKeyword("return"));
        assertTrue(TokenStream.isKeyword("new"));
        assertTrue(TokenStream.isKeyword("delete"));
        assertTrue(TokenStream.isKeyword("typeof"));
        assertTrue(TokenStream.isKeyword("instanceof"));
        assertTrue(TokenStream.isKeyword("void"));
        assertTrue(TokenStream.isKeyword("this"));
        assertTrue(TokenStream.isKeyword("with"));
        assertTrue(TokenStream.isKeyword("true"));
        assertTrue(TokenStream.isKeyword("false"));
        assertTrue(TokenStream.isKeyword("null"));
        assertFalse(TokenStream.isKeyword("test"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("test"));
        assertTrue(TokenStream.isJSIdentifier("test123"));
        assertTrue(TokenStream.isJSIdentifier("test_123"));
        assertTrue(TokenStream.isJSIdentifier("_test"));
        assertFalse(TokenStream.isJSIdentifier("2test"));
        assertFalse(TokenStream.isJSIdentifier("test."));
        assertFalse(TokenStream.isJSIdentifier("test test"));
        assertFalse(TokenStream.isJSIdentifier("test*"));
    }
}