package com.google.javascript.rhino;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Test true cases
        assertTrue(TokenStream.isKeyword("false"));
        assertTrue(TokenStream.isKeyword("null"));
        assertTrue(TokenStream.isKeyword("break"));
        assertTrue(TokenStream.isKeyword("class"));
        assertTrue(TokenStream.isKeyword("const"));
        assertTrue(TokenStream.isKeyword("continue"));
        assertTrue(TokenStream.isKeyword("default"));
        assertTrue(TokenStream.isKeyword("delete"));
        assertTrue(TokenStream.isKeyword("do"));
        assertTrue(TokenStream.isKeyword("else"));
        assertTrue(TokenStream.isKeyword("export"));
        assertTrue(TokenStream.isKeyword("extends"));
        assertTrue(TokenStream.isKeyword("false"));
        assertTrue(TokenStream.isKeyword("finally"));
        assertTrue(TokenStream.isKeyword("float"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("function"));
        assertTrue(TokenStream.isKeyword("goto"));
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("import"));
        assertTrue(TokenStream.isKeyword("in"));
        assertTrue(TokenStream.isKeyword("instanceof"));
        assertTrue(TokenStream.isKeyword("int"));
        assertTrue(TokenStream.isKeyword("interface"));
        assertTrue(TokenStream.isKeyword("let"));
        assertTrue(TokenStream.isKeyword("long"));
        assertTrue(TokenStream.isKeyword("native"));
        assertTrue(TokenStream.isKeyword("new"));
        assertTrue(TokenStream.isKeyword("package"));
        assertTrue(TokenStream.isKeyword("private"));
        assertTrue(TokenStream.isKeyword("protected"));
        assertTrue(TokenStream.isKeyword("return"));
        assertTrue(TokenStream.isKeyword("short"));
        assertTrue(TokenStream.isKeyword("static"));
        assertTrue(TokenStream.isKeyword("super"));
        assertTrue(TokenStream.isKeyword("switch"));
        assertTrue(TokenStream.isKeyword("synchronized"));
        assertTrue(TokenStream.isKeyword("this"));
        assertTrue(TokenStream.isKeyword("throw"));
        assertTrue(TokenStream.isKeyword("throws"));
        assertTrue(TokenStream.isKeyword("transient"));
        assertTrue(TokenStream.isKeyword("try"));
        assertTrue(TokenStream.isKeyword("typeof"));
        assertTrue(TokenStream.isKeyword("var"));
        assertTrue(TokenStream.isKeyword("void"));
        assertTrue(TokenStream.isKeyword("volatile"));
        assertTrue(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("with"));

        // Test false cases
        assertFalse(TokenStream.isKeyword(""));
        assertFalse(TokenStream.isKeyword("abc"));
        assertFalse(TokenStream.isKeyword("123"));
        assertFalse(TokenStream.isKeyword("abc123"));
        assertFalse(TokenStream.isKeyword("_abc"));
        assertFalse(TokenStream.isKeyword("abc_"));
        assertFalse(TokenStream.isKeyword("ab_c"));
        assertFalse(TokenStream.isKeyword("abC"));
    }

    @Test
    public void testIsJSIdentifier() {
        // Test true cases
        assertTrue(TokenStream.isJSIdentifier(""));
        assertTrue(TokenStream.isJSIdentifier("_"));
        assertTrue(TokenStream.isJSIdentifier("abc"));
        assertTrue(TokenStream.isJSIdentifier("ab_c"));
        assertTrue(TokenStream.isJSIdentifier("abC"));
        assertTrue(TokenStream.isJSIdentifier("123"));
        assertTrue(TokenStream.isJSIdentifier("1_2"));
        assertTrue(TokenStream.isJSIdentifier("1A2"));
        assertTrue(TokenStream.isJSIdentifier("_1A2"));
        assertTrue(TokenStream.isJSIdentifier("__1A2"));
        assertTrue(TokenStream.isJSIdentifier("1_23"));
        assertTrue(TokenStream.isJSIdentifier("1_2_3"));
        assertTrue(TokenStream.isJSIdentifier("1_2_3_"));
        assertTrue(TokenStream.isJSIdentifier("1_2_3__"));

        // Test false cases
        assertFalse(TokenStream.isJSIdentifier(""));
        assertFalse(TokenStream.isJSIdentifier("1234"));
        assertFalse(TokenStream.isJSIdentifier("1aBc"));
        assertFalse(TokenStream.isJSIdentifier("_1A2B"));
        assertFalse(TokenStream.isJSIdentifier("1_2B"));
    }
}