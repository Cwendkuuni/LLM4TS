package com.google.javascript.rhino;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("for"));
        assertFalse(TokenStream.isKeyword("xyz"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("varName"));
        assertTrue(TokenStream.isJSIdentifier("_privateVar"));
        assertFalse(TokenStream.isJSIdentifier("1test"));
    }

    @Test
    public void testIsKeyword_EmptyString() {
        assertFalse(TokenStream.isKeyword(""));
    }

    @Test
    public void testIsKeyword_SingleCharacter() {
        assertFalse(TokenStream.isKeyword("a"));
        assertFalse(TokenStream.isKeyword("Z"));
    }

    @Test
    public void testIsKeyword_LongString() {
        assertFalse(TokenStream.isKeyword("thisIsVeryLongStringThatIsNotAKeyword"));
    }

    @Test
    public void testIsKeyword_CaseInsensitive() {
        assertTrue(TokenStream.isKeyword("IF"));
        assertTrue(TokenStream.isKeyword("FOR"));
    }

    @Test
    public void testIsKeyword_SpecialCharacters() {
        assertFalse(TokenStream.isKeyword("@#$"));
        // Add more special characters test cases
    }

    @Test
    public void testIsJSIdentifier_EmptyString() {
        assertFalse(TokenStream.isJSIdentifier(""));
    }

    @Test
    public void testIsJSIdentifier_SingleCharacter() {
        assertTrue(TokenStream.isJSIdentifier("a"));
        assertTrue(TokenStream.isJSIdentifier("_"));
    }

    @Test
    public void testIsJSIdentifier_LongString() {
        assertTrue(TokenStream.isJSIdentifier("thisIsVeryLongStringForIdentifier"));
    }

    @Test
    public void testIsJSIdentifier_CaseInsensitive() {
        assertTrue(TokenStream.isJSIdentifier("VaRnAmE"));
    }

    @Test
    public void testIsJSIdentifier_SpecialCharacters() {
        assertFalse(TokenStream.isJSIdentifier("@#$"));
        // Add more special characters test cases
    }
}