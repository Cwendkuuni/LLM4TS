package com.google.javascript.rhino;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenStreamTest {
    @Test
    public void testIsKeyword() {
        assertTrue(Util.isKeyword("if"));
        assertTrue(Util.isKeyword("for"));
        assertTrue(Util.isKeyword("goto"));
        assertTrue(Util.isKeyword("long"));
        assertTrue(Util.isKeyword("null"));
        assertTrue(Util.isKeyword("void"));
        assertTrue(Util.isKeyword("with"));
        assertTrue(Util.isKeyword("class"));
        assertTrue(Util.isKeyword("break"));
        assertTrue(Util.isKeyword("while"));
        assertTrue(Util.isKeyword("false"));
        assertTrue(Util.isKeyword("const"));
        assertTrue(Util.isKeyword("final"));
        assertTrue(Util.isKeyword("float"));
        assertTrue(Util.isKeyword("short"));
        assertTrue(Util.isKeyword("super"));
        assertTrue(Util.isKeyword("throw"));
        assertTrue(Util.isKeyword("catch"));
        assertTrue(Util.isKeyword("native"));
        assertTrue(Util.isKeyword("delete"));
        assertTrue(Util.isKeyword("return"));
        assertTrue(Util.isKeyword("throws"));
        assertTrue(Util.isKeyword("import"));
        assertTrue(Util.isKeyword("double"));
        assertTrue(Util.isKeyword("static"));
        assertTrue(Util.isKeyword("public"));
        assertTrue(Util.isKeyword("switch"));
        assertTrue(Util.isKeyword("export"));
        assertTrue(Util.isKeyword("typeof"));
        assertTrue(Util.isKeyword("package"));
        assertTrue(Util.isKeyword("default"));
        assertTrue(Util.isKeyword("finally"));
        assertTrue(Util.isKeyword("boolean"));
        assertTrue(Util.isKeyword("private"));
        assertTrue(Util.isKeyword("extends"));
        assertTrue(Util.isKeyword("abstract"));
        assertTrue(Util.isKeyword("continue"));
        assertTrue(Util.isKeyword("debugger"));
        assertTrue(Util.isKeyword("function"));
        assertTrue(Util.isKeyword("volatile"));
        assertTrue(Util.isKeyword("interface"));
        assertTrue(Util.isKeyword("protected"));
        assertTrue(Util.isKeyword("transient"));
        assertTrue(Util.isKeyword("implements"));
        assertTrue(Util.isKeyword("instanceof"));

        assertFalse(Util.isKeyword("abc"));
        assertFalse(Util.isKeyword("123"));
        assertFalse(Util.isKeyword("true"));
        assertFalse(Util.isKeyword("privateclass"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(Util.isJSIdentifier("abc"));
        assertTrue(Util.isJSIdentifier("abc123"));
        assertTrue(Util.isJSIdentifier("aBcDe"));
        assertTrue(Util.isJSIdentifier("_abc"));
        assertTrue(Util.isJSIdentifier("$abc"));
        assertTrue(Util.isJSIdentifier("abc_"));
        assertTrue(Util.isJSIdentifier("abc$"));

        assertFalse(Util.isJSIdentifier("123abc"));
        assertFalse(Util.isJSIdentifier("abc!"));
        assertFalse(Util.isJSIdentifier("abc."));
        assertFalse(Util.isJSIdentifier("abc#"));
        assertFalse(Util.isJSIdentifier("abc?"));
        assertFalse(Util.isJSIdentifier("abc%"));
    }
}