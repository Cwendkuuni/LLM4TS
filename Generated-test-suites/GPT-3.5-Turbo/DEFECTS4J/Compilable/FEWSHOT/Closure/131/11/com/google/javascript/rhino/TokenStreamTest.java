package com.google.javascript.rhino;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenStreamTest {
    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("class"));
        assertTrue(TokenStream.isKeyword("void"));
        assertTrue(TokenStream.isKeyword("false"));
        assertTrue(TokenStream.isKeyword("extends"));
        assertTrue(TokenStream.isKeyword("implements"));
        assertTrue(TokenStream.isKeyword("protected"));
        assertTrue(TokenStream.isKeyword("synchronized"));
        assertTrue(TokenStream.isKeyword("debugger"));
        assertFalse(TokenStream.isKeyword("nokeyword"));
        assertFalse(TokenStream.isKeyword(""));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("abc"));
        assertTrue(TokenStream.isJSIdentifier("foo1"));
        assertTrue(TokenStream.isJSIdentifier("_bar"));
        assertTrue(TokenStream.isJSIdentifier("a_1_b"));
        assertFalse(TokenStream.isJSIdentifier("1abc"));
        assertFalse(TokenStream.isJSIdentifier("$var"));
        assertFalse(TokenStream.isJSIdentifier(""));
    }
}