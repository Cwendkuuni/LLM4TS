package com.google.javascript.rhino;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword_1() {
        assertTrue(TokenStream.isKeyword("if"));
    }

    @Test
    public void testIsKeyword_2() {
        assertTrue(TokenStream.isKeyword("for"));
    }

    @Test
    public void testIsKeyword_3() {
        assertTrue(TokenStream.isKeyword("void"));
    }

    @Test
    public void testIsKeyword_edgeCase() {
        assertFalse(TokenStream.isKeyword("foobar"));
    }

    @Test
    public void testIsKeyword_errorScenario() {
        assertFalse(TokenStream.isKeyword(""));
    }

    @Test
    public void testIsJSIdentifier_1() {
        assertTrue(TokenStream.isJSIdentifier("testVar"));
    }

    @Test
    public void testIsJSIdentifier_2() {
        assertTrue(TokenStream.isJSIdentifier("_privateVar"));
    }

    @Test
    public void testIsJSIdentifier_3() {
        assertFalse(TokenStream.isJSIdentifier("123var"));
    }

    @Test
    public void testIsJSIdentifier_edgeCase() {
        assertTrue(TokenStream.isJSIdentifier("a"));
    }

    @Test
    public void testIsJSIdentifier_errorScenario() {
        assertFalse(TokenStream.isJSIdentifier(""));
    }
}