package com.google.javascript.rhino;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenStreamTest {

    private TokenStream tokenStream;

    @Before
    public void setUp() {
        tokenStream = new TokenStream();
    }

    @Test
    public void testIsKeyword() {
        assertTrue(tokenStream.isKeyword("if"));
        assertTrue(tokenStream.isKeyword("for"));
        assertTrue(tokenStream.isKeyword("new"));
        assertFalse(tokenStream.isKeyword("abc"));
        assertFalse(tokenStream.isKeyword("123"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(tokenStream.isJSIdentifier("abc"));
        assertTrue(tokenStream.isJSIdentifier("abc123"));
        assertFalse(tokenStream.isJSIdentifier("@#$"));
        assertFalse(tokenStream.isJSIdentifier("123"));
    }
}