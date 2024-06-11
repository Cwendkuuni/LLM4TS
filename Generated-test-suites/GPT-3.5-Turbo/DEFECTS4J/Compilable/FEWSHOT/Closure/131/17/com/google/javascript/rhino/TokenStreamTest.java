package com.google.javascript.rhino;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("in"));
        assertTrue(TokenStream.isKeyword("new"));
        assertTrue(TokenStream.isKeyword("void"));
        assertTrue(TokenStream.isKeyword("with"));
        assertTrue(TokenStream.isKeyword("break"));
        assertTrue(TokenStream.isKeyword("false"));
        assertTrue(TokenStream.isKeyword("class"));
        assertTrue(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("return"));
        assertTrue(TokenStream.isKeyword("throw"));
        assertTrue(TokenStream.isKeyword("catch"));
        assertFalse(TokenStream.isKeyword("foo"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("identifier"));
        assertTrue(TokenStream.isJSIdentifier("test123"));
        assertTrue(TokenStream.isJSIdentifier("camelCase"));
        assertFalse(TokenStream.isJSIdentifier("1variable"));
        assertFalse(TokenStream.isJSIdentifier("!test"));
        assertFalse(TokenStream.isJSIdentifier("if"));
    }

}