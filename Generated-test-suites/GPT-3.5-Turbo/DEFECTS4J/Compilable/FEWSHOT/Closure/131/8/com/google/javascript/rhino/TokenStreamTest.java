package com.google.javascript.rhino;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("true"));
        assertTrue(TokenStream.isKeyword("false"));
        assertTrue(TokenStream.isKeyword("null"));
        assertFalse(TokenStream.isKeyword("abc"));
        assertFalse(TokenStream.isKeyword("123"));
        assertFalse(TokenStream.isKeyword("class"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("abc"));
        assertTrue(TokenStream.isJSIdentifier("foo"));
        assertTrue(TokenStream.isJSIdentifier("$bar"));
        assertTrue(TokenStream.isJSIdentifier("_baz"));
        assertFalse(TokenStream.isJSIdentifier("123"));
        assertFalse(TokenStream.isJSIdentifier("if"));
        assertFalse(TokenStream.isJSIdentifier("for"));
    }
}