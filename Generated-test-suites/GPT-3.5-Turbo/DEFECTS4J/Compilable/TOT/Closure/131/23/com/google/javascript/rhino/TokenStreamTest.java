package com.google.javascript.rhino;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // typical use case: keyword "if"
        assertTrue(TokenStream.isKeyword("if"));

        // typical use case: keyword "while"
        assertTrue(TokenStream.isKeyword("while"));

        // typical use case: keyword "final"
        assertTrue(TokenStream.isKeyword("final"));

        // edge case: empty string
        assertFalse(TokenStream.isKeyword(""));

        // edge case: invalid keyword
        assertFalse(TokenStream.isKeyword("abc"));

        // error scenario: null input
        try {
            TokenStream.isKeyword(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected exception
        }
    }

    @Test
    public void testIsJSIdentifier() {
        // typical use case: valid JS identifier "test"
        assertTrue(TokenStream.isJSIdentifier("test"));

        // typical use case: valid JS identifier "myVar"
        assertTrue(TokenStream.isJSIdentifier("myVar"));

        // edge case: empty string
        assertFalse(TokenStream.isJSIdentifier(""));

        // edge case: invalid JS identifier
        assertFalse(TokenStream.isJSIdentifier("1test"));

        // error scenario: null input
        try {
            TokenStream.isJSIdentifier(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected exception
        }
    }
}