package com.google.javascript.rhino;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {
    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("class"));
        assertTrue(TokenStream.isKeyword("break"));
        assertTrue(TokenStream.isKeyword("return"));
        assertTrue(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("false"));
        assertTrue(TokenStream.isKeyword("null"));

        assertFalse(TokenStream.isKeyword("foo"));
        assertFalse(TokenStream.isKeyword("bar"));
        assertFalse(TokenStream.isKeyword("baz"));
        assertFalse(TokenStream.isKeyword("One"));
        assertFalse(TokenStream.isKeyword("Two"));
        assertFalse(TokenStream.isKeyword("Three"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("foo"));
        assertTrue(TokenStream.isJSIdentifier("bar"));
        assertTrue(TokenStream.isJSIdentifier("baz"));
        assertTrue(TokenStream.isJSIdentifier("One"));
        assertTrue(TokenStream.isJSIdentifier("Two"));
        assertTrue(TokenStream.isJSIdentifier("Three"));

        assertFalse(TokenStream.isJSIdentifier("if"));
        assertFalse(TokenStream.isJSIdentifier("for"));
        assertFalse(TokenStream.isJSIdentifier("class"));
        assertFalse(TokenStream.isJSIdentifier("break"));
        assertFalse(TokenStream.isJSIdentifier("return"));
        assertFalse(TokenStream.isJSIdentifier("while"));
        assertFalse(TokenStream.isJSIdentifier("false"));
        assertFalse(TokenStream.isJSIdentifier("null"));
    }
}