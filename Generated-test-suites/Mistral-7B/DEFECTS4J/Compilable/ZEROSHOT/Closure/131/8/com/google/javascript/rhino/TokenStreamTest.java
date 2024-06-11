package com.google.javascript.rhino;
import static org.junit.Assert.*;
import static com.google.javascript.rhino.TokenStream.*;

import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Test true cases
        assertTrue(isKeyword("abstract"));
        assertTrue(isKeyword("arguments"));
        assertTrue(isKeyword("break"));
        assertTrue(isKeyword("byte"));
        assertTrue(isKeyword("case"));
        assertTrue(isKeyword("catch"));
        assertTrue(isKeyword("char"));
        assertTrue(isKeyword("class"));
        assertTrue(isKeyword("const"));
        assertTrue(isKeyword("continue"));
        assertTrue(isKeyword("default"));
        assertTrue(isKeyword("delete"));
        assertTrue(isKeyword("do"));
        assertTrue(isKeyword("double"));
        assertTrue(isKeyword("else"));
        assertTrue(isKeyword("export"));
        assertTrue(isKeyword("extends"));
        assertTrue(isKeyword("false"));
        assertTrue(isKeyword("final"));
        assertTrue(isKeyword("finally"));
        assertTrue(isKeyword("float"));
        assertTrue(isKeyword("for"));
        assertTrue(isKeyword("function"));
        assertTrue(isKeyword("goto"));
        assertTrue(isKeyword("if"));
        assertTrue(isKeyword("implements"));
        assertTrue(isKeyword("import"));
        assertTrue(isKeyword("in"));
        assertTrue(isKeyword("instanceof"));
        assertTrue(isKeyword("int"));
        assertTrue(isKeyword("interface"));
        assertTrue(isKeyword("let"));
        assertTrue(isKeyword("long"));
        assertTrue(isKeyword("native"));
        assertTrue(isKeyword("new"));
        assertTrue(isKeyword("null"));
        assertTrue(isKeyword("package"));
        assertTrue(isKeyword("private"));
        assertTrue(isKeyword("protected"));
        assertTrue(isKeyword("public"));
        assertTrue(isKeyword("return"));
        assertTrue(isKeyword("short"));
        assertTrue(isKeyword("static"));
        assertTrue(isKeyword("super"));
        assertTrue(isKeyword("switch"));
        assertTrue(isKeyword("synchronized"));
        assertTrue(isKeyword("this"));
        assertTrue(isKeyword("throw"));
        assertTrue(isKeyword("throws"));
        assertTrue(isKeyword("transient"));
        assertTrue(isKeyword("try"));
        assertTrue(isKeyword("typeof"));
        assertTrue(isKeyword("var"));
        assertTrue(isKeyword("void"));
        assertTrue(isKeyword("volatile"));
        assertTrue(isKeyword("while"));
        assertTrue(isKeyword("with"));

        // Test false cases
        assertFalse(isKeyword(""));
        assertFalse(isKeyword("abc"));
        assertFalse(isKeyword("123"));
        assertFalse(isKeyword("Test"));
        assertFalse(isKeyword("isKeyword"));
    }

    @Test
    public void testIsJSIdentifier() {
        // Test true cases
        assertTrue(isJSIdentifier(""));
        assertTrue(isJSIdentifier("foo"));
        assertTrue(isJSIdentifier("_foo"));
        assertTrue(isJSIdentifier("fooBar"));
        assertTrue(isJSIdentifier("foo_bar"));
        assertTrue(isJSIdentifier("foo_bar1"));
        assertTrue(isJSIdentifier("foo_bar_123"));
        assertTrue(isJSIdentifier("foo_bar_123_456"));
        assertTrue(isJSIdentifier("foo_bar_123_456_789"));
        assertTrue(isJSIdentifier("foo_bar_123_456_789_012"));

        // Test false cases
        assertFalse(isJSIdentifier(""));
        assertFalse(isJSIdentifier("123"));
        assertFalse(isJSIdentifier("foo123"));
        assertFalse(isJSIdentifier("foo-bar"));
        assertFalse(isJSIdentifier("foo bar"));
        assertFalse(isJSIdentifier("foo bar "));
        assertFalse(isJSIdentifier("foo bar "));
        assertFalse(isJSIdentifier("foo bar "));
        assertFalse(isJSIdentifier("foo bar "));
        assertFalse(isJSIdentifier("foo bar "));
    }
}