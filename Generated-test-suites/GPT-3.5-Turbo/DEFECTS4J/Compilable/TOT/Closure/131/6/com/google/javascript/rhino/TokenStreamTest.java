package com.google.javascript.rhino;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertFalse(TokenStream.isKeyword("test"));
        assertTrue(TokenStream.isKeyword("if"));
        assertFalse(TokenStream.isKeyword("else"));
        assertTrue(TokenStream.isKeyword("void"));
        assertFalse(TokenStream.isKeyword("return"));
        assertTrue(TokenStream.isKeyword("for"));
        assertFalse(TokenStream.isKeyword("while"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("test$name"));
        assertFalse(TokenStream.isJSIdentifier(""));
        assertFalse(TokenStream.isJSIdentifier("123test"));
        assertTrue(TokenStream.isJSIdentifier("first"));
        assertFalse(TokenStream.isJSIdentifier("_underscore"));
        assertTrue(TokenStream.isJSIdentifier("second_underscore"));
        assertFalse(TokenStream.isJSIdentifier("invalid@character"));
    }
}