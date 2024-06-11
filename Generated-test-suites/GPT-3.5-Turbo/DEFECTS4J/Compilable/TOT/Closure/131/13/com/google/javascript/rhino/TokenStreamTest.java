package com.google.javascript.rhino;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.google.javascript.rhino.TokenStream;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // typical use cases
        assertTrue(TokenStream.isKeyword("while"));

        // edge cases
        assertTrue(TokenStream.isKeyword("final"));
        assertTrue(TokenStream.isKeyword("void"));
        assertTrue(TokenStream.isKeyword("boolean"));
        assertTrue(TokenStream.isKeyword("extends"));
        assertTrue(TokenStream.isKeyword("synchronized"));

        // error scenarios
        assertFalse(TokenStream.isKeyword("UndefinedKeyword"));
        assertFalse(TokenStream.isKeyword(""));
    }

    @Test
    public void testIsJSIdentifier() {
        // typical use cases
        assertTrue(TokenStream.isJSIdentifier("variableName"));
        assertTrue(TokenStream.isJSIdentifier("_underscore"));
        assertTrue(TokenStream.isJSIdentifier("camelCase"));

        // edge cases
        assertTrue(TokenStream.isJSIdentifier("a"));
        assertTrue(TokenStream.isJSIdentifier("i"));

        // error scenarios
        assertFalse(TokenStream.isJSIdentifier("123InvalidIdentifier"));
        assertFalse(TokenStream.isJSIdentifier("Contains Spaces"));
        assertFalse(TokenStream.isJSIdentifier(""));

    }
}