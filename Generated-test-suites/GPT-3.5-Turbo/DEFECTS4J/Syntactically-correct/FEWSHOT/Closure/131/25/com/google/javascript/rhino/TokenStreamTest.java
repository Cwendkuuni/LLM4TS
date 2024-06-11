package com.google.javascript.rhino;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenStreamTest {
    
    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("in"));
        assertTrue(TokenStream.isKeyword("do"));
        assertTrue(TokenStream.isKeyword("null"));
        assertTrue(TokenStream.isKeyword("class"));
        assertTrue(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("false"));
        assertTrue(TokenStream.isKeyword("const"));
        assertTrue(TokenStream.isKeyword("final"));
        assertTrue(TokenStream.isKeyword("float"));
        assertTrue(TokenStream.isKeyword("short"));
        assertTrue(TokenStream.isKeyword("super"));
        assertTrue(TokenStream.isKeyword("throw"));
        assertTrue(TokenStream.isKeyword("catch"));
        assertTrue(TokenStream.isKeyword("native"));
        assertTrue(TokenStream.isKeyword("delete"));
        assertTrue(TokenStream.isKeyword("return"));
        assertTrue(TokenStream.isKeyword("throws"));
        assertTrue(TokenStream.isKeyword("import"));
        assertTrue(TokenStream.isKeyword("double"));
        assertTrue(TokenStream.isKeyword("static"));
        assertTrue(TokenStream.isKeyword("public"));
        assertTrue(TokenStream.isKeyword("switch"));
        assertTrue(TokenStream.isKeyword("export"));
        assertTrue(TokenStream.isKeyword("typeof"));
        assertTrue(TokenStream.isKeyword("package"));
        assertTrue(TokenStream.isKeyword("default"));
        assertTrue(TokenStream.isKeyword("finally"));
        assertTrue(TokenStream.isKeyword("boolean"));
        assertTrue(TokenStream.isKeyword("private"));
        assertTrue(TokenStream.isKeyword("extends"));
        assertTrue(TokenStream.isKeyword("abstract"));
        assertTrue(TokenStream.isKeyword("continue"));
        assertTrue(TokenStream.isKeyword("debugger"));
        assertTrue(TokenStream.isKeyword("function"));
        assertTrue(TokenStream.isKeyword("volatile"));
        assertTrue(TokenStream.isKeyword("interface"));
        assertTrue(TokenStream.isKeyword("protected"));
        assertTrue(TokenStream.isKeyword("transient"));
        assertTrue(TokenStream.isKeyword("implements"));
        assertTrue(TokenStream.isKeyword("instanceof"));
        assertTrue(TokenStream.isKeyword("synchronized"));
        assertFalse(TokenStream.isKeyword("xyz"));
    }
    
    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("abc"));
        assertTrue(TokenStream.isJSIdentifier("x123"));
        assertTrue(TokenStream.isJSIdentifier("_abc"));
        assertFalse(TokenStream.isJSIdentifier("123abc"));
        assertFalse(TokenStream.isJSIdentifier("abc@"));
    }
}