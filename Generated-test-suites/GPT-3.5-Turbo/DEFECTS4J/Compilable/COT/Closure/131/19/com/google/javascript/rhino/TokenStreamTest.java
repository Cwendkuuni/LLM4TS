package com.google.javascript.rhino;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertFalse(TokenStream.isKeyword("hello"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("name"));
        assertFalse(TokenStream.isJSIdentifier("123"));
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testIsKeyword_NullInput() {
        assertFalse(TokenStream.isKeyword(null));
    }

    @Test
    public void testIsJSIdentifier_BlankInput() {
        assertFalse(TokenStream.isJSIdentifier(""));
    }

    @Test
    public void testIsJSIdentifier_InvalidIdentifier() {
        assertFalse(TokenStream.isJSIdentifier("test-identifier"));
    }

    @Test
    public void testIsJSIdentifier_StartWithNumber() {
        assertFalse(TokenStream.isJSIdentifier("123identifier"));
    }

    @Test
    public void testIsJSIdentifier_ContainsSpecialCharacter() {
        assertFalse(TokenStream.isJSIdentifier("test_identifier"));
    }
}