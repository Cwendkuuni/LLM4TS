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
    public void testIsKeyword_positive() {
        assertTrue(tokenStream.isKeyword("if"));
    }

    @Test
    public void testIsKeyword_negative() {
        assertFalse(tokenStream.isKeyword("hello"));
    }

    @Test
    public void testIsJSIdentifier_valid() {
        assertTrue(tokenStream.isJSIdentifier("validIdentifier"));
    }

    @Test
    public void testIsJSIdentifier_invalid() {
        assertFalse(tokenStream.isJSIdentifier("9invalid"));
    }

    @Test
    public void testIsJSIdentifier_emptyString() {
        assertFalse(tokenStream.isJSIdentifier(""));
    }
}