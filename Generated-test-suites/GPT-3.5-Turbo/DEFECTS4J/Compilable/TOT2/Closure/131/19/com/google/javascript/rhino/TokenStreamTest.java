package com.google.javascript.rhino;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if")); // typical use case
        assertTrue(TokenStream.isKeyword("delete")); // edge case
        assertFalse(TokenStream.isKeyword("xyz")); // non-keyword case
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("validIdentifier")); // typical use case
        assertTrue(TokenStream.isJSIdentifier("num123")); // edge case
        assertFalse(TokenStream.isJSIdentifier("123num")); // non-identifier case
    }
}