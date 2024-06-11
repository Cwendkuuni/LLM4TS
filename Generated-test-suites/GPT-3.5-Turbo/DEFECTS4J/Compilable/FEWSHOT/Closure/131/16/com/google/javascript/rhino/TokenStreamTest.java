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
        assertTrue(TokenStream.isKeyword("break"));
        assertTrue(TokenStream.isKeyword("continue"));
        assertTrue(TokenStream.isKeyword("return"));
        assertTrue(TokenStream.isKeyword("switch"));
        assertTrue(TokenStream.isKeyword("case"));
        assertTrue(TokenStream.isKeyword("default"));
        assertTrue(TokenStream.isKeyword("do"));
        assertTrue(TokenStream.isKeyword("try"));
        assertTrue(TokenStream.isKeyword("catch"));
        assertTrue(TokenStream.isKeyword("finally"));
        assertTrue(TokenStream.isKeyword("throw"));
        assertTrue(TokenStream.isKeyword("with"));
        assertTrue(TokenStream.isKeyword("instanceof"));
        assertTrue(TokenStream.isKeyword("debugger"));
        assertTrue(TokenStream.isKeyword("typeof"));
        assertTrue(TokenStream.isKeyword("new"));
        assertTrue(TokenStream.isKeyword("delete"));
        assertTrue(TokenStream.isKeyword("void"));
        assertTrue(TokenStream.isKeyword("else"));
        assertTrue(TokenStream.isKeyword("true"));
        assertTrue(TokenStream.isKeyword("false"));
        assertTrue(TokenStream.isKeyword("null"));

        assertFalse(TokenStream.isKeyword("foo"));
        assertFalse(TokenStream.isKeyword("bar"));
        assertFalse(TokenStream.isKeyword("baz"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("foo"));
        assertTrue(TokenStream.isJSIdentifier("bar"));
        assertTrue(TokenStream.isJSIdentifier("baz"));
        assertTrue(TokenStream.isJSIdentifier("helloWorld"));
        assertTrue(TokenStream.isJSIdentifier("_privateVariable"));
        assertTrue(TokenStream.isJSIdentifier("$specialVar"));

        assertFalse(TokenStream.isJSIdentifier("123invalid"));
        assertFalse(TokenStream.isJSIdentifier("do"));
        assertFalse(TokenStream.isJSIdentifier("true"));
        assertFalse(TokenStream.isJSIdentifier("false"));
        assertFalse(TokenStream.isJSIdentifier("null"));
    }
}