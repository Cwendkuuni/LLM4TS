package com.google.javascript.rhino;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("true"));
        assertTrue(TokenStream.isKeyword("false"));
        assertTrue(TokenStream.isKeyword("null"));
        assertTrue(TokenStream.isKeyword("break"));
        assertTrue(TokenStream.isKeyword("continue"));
        assertTrue(TokenStream.isKeyword("function"));
        assertTrue(TokenStream.isKeyword("package"));
        assertTrue(TokenStream.isKeyword("interface"));
        assertTrue(TokenStream.isKeyword("implements"));
        assertTrue(TokenStream.isKeyword("instanceof"));
        assertTrue(TokenStream.isKeyword("synchronized"));
        assertFalse(TokenStream.isKeyword(""));
        assertFalse(TokenStream.isKeyword("abc"));
        assertFalse(TokenStream.isKeyword("abc123"));
        assertFalse(TokenStream.isKeyword("123"));
        assertFalse(TokenStream.isKeyword("c123"));
        assertFalse(TokenStream.isKeyword("#abc"));
        assertFalse(TokenStream.isKeyword("$abc"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier(""));
        assertTrue(TokenStream.isJSIdentifier("abc"));
        assertTrue(TokenStream.isJSIdentifier("abc123"));
        assertFalse(TokenStream.isJSIdentifier("123"));
        assertFalse(TokenStream.isJSIdentifier("c123"));
        assertFalse(TokenStream.isJSIdentifier("#abc"));
        assertFalse(TokenStream.isJSIdentifier("$abc"));
    }
}