package com.google.javascript.rhino;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword_short() {
        // Test keywords of length <= 3
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("else"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("true"));
        assertTrue(TokenStream.isKeyword("false"));
        assertTrue(TokenStream.isKeyword("class"));
        assertTrue(TokenStream.isKeyword("break"));
        assertTrue(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("null"));
        assertTrue(TokenStream.isKeyword("void"));
        assertTrue(TokenStream.isKeyword("with"));
        assertTrue(TokenStream.isKeyword("goto"));
        assertFalse(TokenStream.isKeyword("abc"));
    }

    @Test
    public void testIsKeyword_long() {
        // Test keywords of length > 3
        assertTrue(TokenStream.isKeyword("byte"));
        assertTrue(TokenStream.isKeyword("short"));
        assertTrue(TokenStream.isKeyword("long"));
        assertTrue(TokenStream.isKeyword("float"));
        assertTrue(TokenStream.isKeyword("double"));
        assertTrue(TokenStream.isKeyword("integer"));
        assertTrue(TokenStream.isKeyword("boolean"));
        assertTrue(TokenStream.isKeyword("Object"));
        assertTrue(TokenStream.isKeyword("String"));
        assertTrue(TokenStream.isKeyword("null"));
        assertTrue(TokenStream.isKeyword("Array"));
        assertTrue(TokenStream.isKeyword("Function"));
        assertTrue(TokenStream.isKeyword("Number"));
        assertTrue(TokenStream.isKeyword("Date"));
        assertTrue(TokenStream.isKeyword("RegExp"));
        assertTrue(TokenStream.isKeyword("Error"));
        assertFalse(TokenStream.isKeyword("MyClass"));
    }

    @Test
    public void testIsJSIdentifier() {
        // Test JavaScript identifiers
        assertTrue(TokenStream.isJSIdentifier("foo"));
        assertTrue(TokenStream.isJSIdentifier("_bar"));
        assertTrue(TokenStream.isJSIdentifier("myVar"));
        assertTrue(TokenStream.isJSIdentifier("my_long_variable_name"));
        assertTrue(TokenStream.isJSIdentifier("myVariableNameWithNumbers_123"));
        assertTrue(TokenStream.isJSIdentifier("myVariableNameWithNumbers_123_abc"));
        assertTrue(TokenStream.isJSIdentifier("myVariableNameWithNumbers_123_abc_def"));
        assertFalse(TokenStream.isJSIdentifier("myVariableNameWithNumbers_123_abc_def_45")); // too long
        assertFalse(TokenStream.isJSIdentifier("myVariableNameWithNumbers_123_abc_def-")); // invalid character
        assertFalse(TokenStream.isJSIdentifier("myVariableNameWithNumbers_123_abc_def__")); // invalid character
    }
}