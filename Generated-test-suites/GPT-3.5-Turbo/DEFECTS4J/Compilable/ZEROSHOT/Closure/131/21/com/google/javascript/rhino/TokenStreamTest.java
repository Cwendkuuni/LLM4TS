package com.google.javascript.rhino;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeywordValid() {
        assertTrue(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("do"));
        assertFalse(TokenStream.isKeyword("foobar"));
        assertFalse(TokenStream.isKeyword("1234"));
    }

    @Test
    public void testIsJSIdentifierValid() {
        assertTrue(TokenStream.isJSIdentifier("isValid"));
        assertTrue(TokenStream.isJSIdentifier("testVariable"));
        assertFalse(TokenStream.isJSIdentifier("1test"));
        assertFalse(TokenStream.isJSIdentifier("test!"));
    }
}