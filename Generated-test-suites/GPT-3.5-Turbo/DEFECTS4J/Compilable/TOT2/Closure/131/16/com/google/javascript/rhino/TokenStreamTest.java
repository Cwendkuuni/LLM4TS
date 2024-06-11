package com.google.javascript.rhino;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("function"));
        assertFalse(TokenStream.isKeyword("test"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("validIdentifier"));
        assertTrue(TokenStream.isJSIdentifier("anotherValid123"));
        assertFalse(TokenStream.isJSIdentifier("123invalid"));
    }

    @Test
    public void testIsJSIdentifierEdgeCases() {
        assertTrue(TokenStream.isJSIdentifier("a")); // Single character identifier
        assertTrue(TokenStream.isJSIdentifier("")); // Empty identifier
    }
}