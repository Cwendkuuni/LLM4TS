package com.google.javascript.rhino;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertFalse(TokenStream.isKeyword("foo"));
    }
    
    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("foobar"));
        assertFalse(TokenStream.isJSIdentifier("foo bar"));
    }
}