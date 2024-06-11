package com.google.javascript.rhino;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertFalse(TokenStream.isKeyword("test"));
        assertTrue(TokenStream.isKeyword("long"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("validIdentifier"));
        assertFalse(TokenStream.isJSIdentifier("123Invalid"));
        assertTrue(TokenStream.isJSIdentifier("_underscore"));
    }

    @Test
    public void testIsKeywordEdgeCases() {
        assertFalse(TokenStream.isKeyword(""));
        assertTrue(TokenStream.isKeyword("null"));
        assertFalse(TokenStream.isKeyword("string"));
    }

    @Test
    public void testIsJSIdentifierEdgeCases() {
        assertFalse(TokenStream.isJSIdentifier(""));
        assertTrue(TokenStream.isJSIdentifier("validIdentifier123"));
        assertFalse(TokenStream.isJSIdentifier("#invalidIdentifier"));
    }

    @Test(expected = NullPointerException.class)
    public void testIsKeywordNull() {
        TokenStream.isKeyword(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsJSIdentifierSpecialChar() {
        TokenStream.isJSIdentifier("invalid#identifier");
    }
}