package com.google.javascript.rhino;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenStreamTest {
    @Test
    public void testIsKeyword() {
        // Test positive cases
        assertTrue(TokenStream.isKeyword("break"));
        assertTrue(TokenStream.isKeyword("class"));
        assertTrue(TokenStream.isKeyword("continue"));
        assertTrue(TokenStream.isKeyword("default"));
        assertTrue(TokenStream.isKeyword("delete"));
        assertTrue(TokenStream.isKeyword("do"));
        assertTrue(TokenStream.isKeyword("else"));
        assertTrue(TokenStream.isKeyword("export"));
        assertTrue(TokenStream.isKeyword("false"));
        assertTrue(TokenStream.isKeyword("finally"));
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

        // Test negative cases
        assertFalse(TokenStream.isKeyword(""));
        assertFalse(TokenStream.isKeyword(" "));
        assertFalse(TokenStream.isKeyword("1"));
        assertFalse(TokenStream.isKeyword("1foo"));
        assertFalse(TokenStream.isKeyword("foo1"));
        assertFalse(TokenStream.isKeyword("fooBar"));
        assertFalse(TokenStream.isKeyword("barFoo"));
        assertFalse(TokenStream.isKeyword("myVar"));
        assertFalse(TokenStream.isKeyword("MyVar"));
        assertFalse(TokenStream.isKeyword("my_var"));
        assertFalse(TokenStream.isKeyword("My_var"));
        assertFalse(TokenStream.isKeyword("myVar1"));
        assertFalse(TokenStream.isKeyword("MyVar1"));
        assertFalse(TokenStream.isKeyword("myVar_1"));
        assertFalse(TokenStream.isKeyword("MyVar_1"));
        assertFalse(TokenStream.isKeyword("myVar123"));
        assertFalse(TokenStream.isKeyword("MyVar123"));
        assertFalse(TokenStream.isKeyword("myVar_123"));
        assertFalse(TokenStream.isKeyword("MyVar_123"));
        assertFalse(TokenStream.isKeyword("myVar_123x"));
        assertFalse(TokenStream.isKeyword("MyVar_123x"));
    }

    @Test
    public void testIsJSIdentifier() {
        // Test positive cases
        assertTrue(TokenStream.isJSIdentifier(""));
        assertTrue(TokenStream.isJSIdentifier("foo"));
        assertTrue(TokenStream.isJSIdentifier("myVar"));
        assertTrue(TokenStream.isJSIdentifier("my_var"));
        assertTrue(TokenStream.isJSIdentifier("myVar1"));
        assertTrue(TokenStream.isJSIdentifier("MyVar1"));
        assertTrue(TokenStream.isJSIdentifier("myVar_1"));
        assertTrue(TokenStream.isJSIdentifier("MyVar_1"));
        assertTrue(TokenStream.isJSIdentifier("myVar123"));
        assertTrue(TokenStream.isJSIdentifier("MyVar123"));
        assertTrue(TokenStream.isJSIdentifier("myVar_123"));
        assertTrue(TokenStream.isJSIdentifier("MyVar_123"));
        assertTrue(TokenStream.isJSIdentifier("myVar_123x"));
        assertTrue(TokenStream.isJSIdentifier("MyVar_123x"));

        // Test negative cases
        assertFalse(TokenStream.isJSIdentifier("1"));
        assertFalse(TokenStream.isJSIdentifier("1foo"));
        assertFalse(TokenStream.isJSIdentifier("foo1"));
        assertFalse(TokenStream.isJSIdentifier("foo Bar"));
        assertFalse(TokenStream.isJSIdentifier("bar Foo"));
        assertFalse(TokenStream.isJSIdentifier("myVar_123 "));
        assertFalse(TokenStream.isJSIdentifier("my Var"));
        assertFalse(TokenStream.isJSIdentifier("my V a r"));
        assertFalse(TokenStream.isJSIdentifier("my Va r"));
        assertFalse(TokenStream.isJSIdentifier("my Va r_1"));
        assertFalse(TokenStream.isJSIdentifier("my Va r_123"));
        assertFalse(TokenStream.isJSIdentifier("my Va r_123x"));
    }
}