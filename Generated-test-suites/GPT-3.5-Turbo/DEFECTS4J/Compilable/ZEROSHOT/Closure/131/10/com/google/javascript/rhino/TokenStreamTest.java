package com.google.javascript.rhino;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("while"));
        assertFalse(TokenStream.isKeyword("invalid"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("test"));
        assertTrue(TokenStream.isJSIdentifier("_test"));
        assertFalse(TokenStream.isJSIdentifier("1test"));
    }

}