package com.google.javascript.rhino;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("for"));
        assertFalse(TokenStream.isKeyword("abc"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertFalse(TokenStream.isJSIdentifier(""));
        assertTrue(TokenStream.isJSIdentifier("a"));
        assertFalse(TokenStream.isJSIdentifier("1"));
        assertFalse(TokenStream.isJSIdentifier("1abc"));
        assertFalse(TokenStream.isJSIdentifier("@#"));
        assertTrue(TokenStream.isJSIdentifier("valid_identifier"));
        assertFalse(TokenStream.isJSIdentifier("int"));
    }
}

