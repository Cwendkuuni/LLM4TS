package com.google.javascript.rhino;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Typical use case
        assertTrue(TokenStream.isKeyword("if"));

        // Edge cases
        assertTrue(TokenStream.isKeyword("void"));
        assertTrue(TokenStream.isKeyword("final"));
        assertTrue(TokenStream.isKeyword("byte"));
        assertTrue(TokenStream.isKeyword("class"));
        assertTrue(TokenStream.isKeyword("boolean"));

        // Error scenario
        assertFalse(TokenStream.isKeyword("abcd"));
    }

    @Test
    public void testIsJSIdentifier() {
        // Typical use case
        assertTrue(TokenStream.isJSIdentifier("variableName"));

        // Edge cases
        assertTrue(TokenStream.isJSIdentifier("a"));
        assertTrue(TokenStream.isJSIdentifier("ABC123"));
        assertTrue(TokenStream.isJSIdentifier("this"));

        // Error scenario
        assertFalse(TokenStream.isJSIdentifier("123abc"));
        assertFalse(TokenStream.isJSIdentifier("@#$%"));
    }
}