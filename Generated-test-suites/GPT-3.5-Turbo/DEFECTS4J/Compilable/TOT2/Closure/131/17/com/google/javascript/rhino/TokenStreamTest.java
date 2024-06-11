package com.google.javascript.rhino;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("null"));
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("break"));
        assertFalse(TokenStream.isKeyword("for"));
        assertFalse(TokenStream.isKeyword("while"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("validIdentifier"));
        assertTrue(TokenStream.isJSIdentifier("anotherValid123"));
        assertFalse(TokenStream.isJSIdentifier("inv@lid"));
        assertFalse(TokenStream.isJSIdentifier("123"));
    }

}