package com.google.javascript.rhino;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("new"));
        assertFalse(TokenStream.isKeyword("hello"));
        assertFalse(TokenStream.isKeyword("world"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("test"));
        assertTrue(TokenStream.isJSIdentifier("helloWorld"));
        assertTrue(TokenStream.isJSIdentifier("_private"));
        assertFalse(TokenStream.isJSIdentifier("5test"));
        assertFalse(TokenStream.isJSIdentifier("break"));
    }
}