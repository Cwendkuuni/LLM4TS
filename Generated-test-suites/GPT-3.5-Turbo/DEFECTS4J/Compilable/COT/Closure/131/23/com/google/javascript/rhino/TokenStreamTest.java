package com.google.javascript.rhino;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertFalse(TokenStream.isKeyword("abc"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("abc"));
        assertFalse(TokenStream.isJSIdentifier("123"));
    }

    @Test
    public void testIsKeywordEdgeCase() {
        assertFalse(TokenStream.isKeyword(""));
        assertFalse(TokenStream.isKeyword("x"));
    }

    @Test
    public void testIsJSIdentifierEdgeCase() {
        assertFalse(TokenStream.isJSIdentifier(""));
        assertFalse(TokenStream.isJSIdentifier("1x"));
    }

    @Test
    public void testIsKeywordException() {
        try {
            TokenStream.isKeyword(null);
            fail("Expected exception not thrown");
        } catch (NullPointerException e) {
            // expected exception
        }
    }

    @Test
    public void testIsJSIdentifierException() {
        try {
            TokenStream.isJSIdentifier(null);
            fail("Expected exception not thrown");
        } catch (NullPointerException e) {
            // expected exception
        }
    }
}

