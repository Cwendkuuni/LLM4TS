package com.google.javascript.rhino;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if")); //typical use case
        assertTrue(TokenStream.isKeyword("for")); //typical use case
        assertTrue(TokenStream.isKeyword("void")); //typical use case
        assertTrue(TokenStream.isKeyword("public")); //typical use case

        assertFalse(TokenStream.isKeyword("foo")); //typical use case
        assertFalse(TokenStream.isKeyword("bar")); //typical use case
        assertFalse(TokenStream.isKeyword("if1")); //edge case
        assertFalse(TokenStream.isKeyword("Foo")); //edge case
        assertFalse(TokenStream.isKeyword("public1")); //edge case
        assertFalse(TokenStream.isKeyword("foo bar")); //error scenario
        assertFalse(TokenStream.isKeyword("")); //error scenario
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("hello")); //typical use case
        assertTrue(TokenStream.isJSIdentifier("myVariable")); //typical use case
        assertTrue(TokenStream.isJSIdentifier("_private")); //typical use case
        assertTrue(TokenStream.isJSIdentifier("$price")); //typical use case

        assertFalse(TokenStream.isJSIdentifier("123")); //typical use case
        assertFalse(TokenStream.isJSIdentifier("1foo")); //edge case
        assertFalse(TokenStream.isJSIdentifier("my Variable")); //error scenario
        assertFalse(TokenStream.isJSIdentifier("")); //error scenario
    }

}