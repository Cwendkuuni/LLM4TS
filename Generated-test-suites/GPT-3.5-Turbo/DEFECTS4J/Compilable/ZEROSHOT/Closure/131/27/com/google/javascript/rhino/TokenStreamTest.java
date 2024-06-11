package com.google.javascript.rhino;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Test case 1: keyword "if"
        assertTrue(TokenStream.isKeyword("if"));

        // Test case 2: keyword "for"
        assertTrue(TokenStream.isKeyword("for"));

        // Test case 3: keyword "while"
        assertTrue(TokenStream.isKeyword("while"));

        // Test case 4: keyword "class"
        assertTrue(TokenStream.isKeyword("class"));

        // Test case 5: keyword "abstract"
        assertTrue(TokenStream.isKeyword("abstract"));

        // Test case 6: keyword "volatile"
        assertTrue(TokenStream.isKeyword("volatile"));

        // Test case 7: keyword "function"
        assertTrue(TokenStream.isKeyword("function"));

        // Test case 8: keyword "protected"
        assertTrue(TokenStream.isKeyword("protected"));

        // Test case 9: keyword "instanceof"
        assertTrue(TokenStream.isKeyword("instanceof"));

        // Test case 10: keyword "synchronized"
        assertTrue(TokenStream.isKeyword("synchronized"));

        // Test case 11: non-keyword String
        assertFalse(TokenStream.isKeyword("hello"));
    }

    @Test
    public void testIsJSIdentifier() {
        // Test case 1: valid JS identifier "name"
        assertTrue(TokenStream.isJSIdentifier("name"));

        // Test case 2: valid JS identifier "_name"
        assertTrue(TokenStream.isJSIdentifier("_name"));

        // Test case 3: valid JS identifier "name1"
        assertTrue(TokenStream.isJSIdentifier("name1"));

        // Test case 4: valid JS identifier "$name"
        assertTrue(TokenStream.isJSIdentifier("$name"));

        // Test case 5: non-valid JS identifier "123name"
        assertFalse(TokenStream.isJSIdentifier("123name"));

        // Test case 6: non-valid JS identifier "name$"
        assertFalse(TokenStream.isJSIdentifier("name$"));

        // Test case 7: non-valid JS identifier "name@"
        assertFalse(TokenStream.isJSIdentifier("name@"));

        // Test case 8: non-valid JS identifier "name!"
        assertFalse(TokenStream.isJSIdentifier("name!"));
    }
}