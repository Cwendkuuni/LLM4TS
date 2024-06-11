package com.google.javascript.rhino;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword_validKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
    }
    
    @Test
    public void testIsKeyword_invalidKeyword() {
        assertFalse(TokenStream.isKeyword("hello"));
    }

    @Test
    public void testIsJSIdentifier_validIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("variable"));
    }
    
    @Test
    public void testIsJSIdentifier_invalidIdentifier() {
        assertFalse(TokenStream.isJSIdentifier("123variable"));
    }
}