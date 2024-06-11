package com.google.javascript.rhino;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Test true cases
        assertTrue(TokenStream.isKeyword("abstract"));
        assertTrue(TokenStream.isKeyword("as"));
        assertTrue(TokenStream.isKeyword("boolean"));
        assertTrue(TokenStream.isKeyword("break"));
        assertTrue(TokenStream.isKeyword("byte"));
        assertTrue(TokenStream.isKeyword("case"));
        assertTrue(TokenStream.isKeyword("catch"));
        assertTrue(TokenStream.isKeyword("class"));
        assertTrue(TokenStream.isKeyword("const"));
        assertTrue(TokenStream.isKeyword("continue"));
        assertTrue(TokenStream.isKeyword("debugger"));
        assertTrue(TokenStream.isKeyword("default"));
        assertTrue(TokenStream.isKeyword("delete"));
        assertTrue(TokenStream.isKeyword("do"));
        assertTrue(TokenStream.isKeyword("double"));
        assertTrue(TokenStream.isKeyword("else"));
        assertTrue(TokenStream.isKeyword("export"));
        assertTrue(TokenStream.isKeyword("extends"));
        assertTrue(TokenStream.isKeyword("false"));
        assertTrue(TokenStream.isKeyword("final"));
        assertTrue(TokenStream.isKeyword("finally"));
        assertTrue(TokenStream.isKeyword("float"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("function"));
        assertTrue(TokenStream.isKeyword("goto"));
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("implements"));
        assertTrue(TokenStream.isKeyword("import"));
        assertTrue(TokenStream.isKeyword("in"));
        assertTrue(TokenStream.isKeyword("instanceof"));
        assertTrue(TokenStream.isKeyword("int"));
        assertTrue(TokenStream.isKeyword("interface"));
        assertTrue(TokenStream.isKeyword("let"));
        assertTrue(TokenStream.isKeyword("long"));
        assertTrue(TokenStream.isKeyword("native"));
        assertTrue(TokenStream.isKeyword("new"));
        assertTrue(TokenStream.isKeyword("null"));
        assertTrue(TokenStream.isKeyword("package"));
        assertTrue(TokenStream.isKeyword("private"));
        assertTrue(TokenStream.isKeyword("protected"));
        assertTrue(TokenStream.isKeyword("public"));
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
        assertTrue(TokenStream.isKeyword("true"));
        assertTrue(TokenStream.isKeyword("try"));
        assertTrue(TokenStream.isKeyword("typeof"));
        assertTrue(TokenStream.isKeyword("var"));
        assertTrue(TokenStream.isKeyword("void"));
        assertTrue(TokenStream.isKeyword("volatile"));
        assertTrue(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("with"));

        // Test false cases
        assertFalse(TokenStream.isKeyword(""));
        assertFalse(TokenStream.isKeyword("__"));
        assertFalse(TokenStream.isKeyword("abc"));
        assertFalse(TokenStream.isKeyword("123"));
        assertFalse(TokenStream.isKeyword("!@#"));
    }

    @Test
    public void testIsJSIdentifier() {
        // Test true cases
        assertTrue(TokenStream.isJSIdentifier(""));
        assertTrue(TokenStream.isJSIdentifier("foo"));
        assertTrue(TokenStream.isJSIdentifier("bar_baz"));
        assertTrue(TokenStream.isJSIdentifier("x_yz"));
        assertTrue(TokenStream.isJSIdentifier("fooBar"));
        assertTrue(TokenStream.isJSIdentifier("foo_bar"));
        assertTrue(TokenStream.isJSIdentifier("foo_bar_baz"));
        assertTrue(TokenStream.isJSIdentifier("foo_bar_baz_"));
        assertTrue(TokenStream.isJSIdentifier("foo_bar_baz__"));
        assertTrue(TokenStream.isJSIdentifier("foo_bar_baz__1"));

        // Test false cases
        assertFalse(TokenStream.isJSIdentifier(""));
        assertFalse(TokenStream.isJSIdentifier("__"));
        assertFalse(TokenStream.isJSIdentifier("123"));
        assertFalse(TokenStream.isJSIdentifier("!@#"));
        assertFalse(TokenStream.isJSIdentifier("foo bar"));
        assertFalse(TokenStream.isJSIdentifier("foo bar "));
        assertFalse(TokenStream.isJSIdentifier("foo bar_baz"));
        assertFalse(TokenStream.isJSIdentifier("foo bar_baz "));
        assertFalse(TokenStream.isJSIdentifier("foo bar_baz_"));
        assertFalse(TokenStream.isJSIdentifier("foo bar_baz__"));
        assertFalse(TokenStream.isJSIdentifier("foo bar_baz__1"));
        assertFalse(TokenStream.isJSIdentifier("foo bar_baz__1 "));
    }
}