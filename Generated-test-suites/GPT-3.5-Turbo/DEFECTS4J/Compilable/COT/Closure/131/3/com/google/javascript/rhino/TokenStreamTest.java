package com.google.javascript.rhino;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {

    // Test isKeyword method
    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertFalse(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("for"));
        assertFalse(TokenStream.isKeyword("test"));
        assertTrue(TokenStream.isKeyword("void"));
    }

    // Test isJSIdentifier method
    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("test"));
        assertFalse(TokenStream.isJSIdentifier("2test"));
        assertTrue(TokenStream.isJSIdentifier("$_test"));
        assertFalse(TokenStream.isJSIdentifier("if"));
        assertFalse(TokenStream.isJSIdentifier("long"));
    }

}