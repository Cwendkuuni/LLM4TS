package com.google.javascript.rhino;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Test for valid keywords
        assertTrue(TokenStream.isKeyword("byte"));
        assertTrue(TokenStream.isKeyword("char"));
        assertTrue(TokenStream.isKeyword("short"));
        assertTrue(TokenStream.isKeyword("int"));
        assertTrue(TokenStream.isKeyword("long"));
        assertTrue(TokenStream.isKeyword("float"));
        assertTrue(TokenStream.isKeyword("double"));
        assertTrue(TokenStream.isKeyword("boolean"));
        assertTrue(TokenStream.isKeyword("void"));
        assertTrue(TokenStream.isKeyword("null"));
        assertTrue(TokenStream.isKeyword("true"));
        assertTrue(TokenStream.isKeyword("false"));
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("else"));
        assertTrue(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("do"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("switch"));
        assertTrue(TokenStream.isKeyword("case"));
        assertTrue(TokenStream.isKeyword("default"));
        assertTrue(TokenStream.isKeyword("break"));
        assertTrue(TokenStream.isKeyword("continue"));
        assertTrue(TokenStream.isKeyword("return"));
        assertTrue(TokenStream.isKeyword("try"));
        assertTrue(TokenStream.isKeyword("catch"));
        assertTrue(TokenStream.isKeyword("finally"));
        assertTrue(TokenStream.isKeyword("throw"));
        assertTrue(TokenStream.isKeyword("throws"));
        assertTrue(TokenStream.isKeyword("new"));
        assertTrue(TokenStream.isKeyword("instanceof"));
        assertTrue(TokenStream.isKeyword("super"));
        assertTrue(TokenStream.isKeyword("extends"));
        assertTrue(TokenStream.isKeyword("implements"));
        assertTrue(TokenStream.isKeyword("abstract"));
        assertTrue(TokenStream.isKeyword("final"));
        assertTrue(TokenStream.isKeyword("native"));
        assertTrue(TokenStream.isKeyword("static"));
        assertTrue(TokenStream.isKeyword("synchronized"));
        assertTrue(TokenStream.isKeyword("transient"));
        assertTrue(TokenStream.isKeyword("volatile"));
        assertTrue(TokenStream.isKeyword("package"));
        assertTrue(TokenStream.isKeyword("import"));
        assertTrue(TokenStream.isKeyword("class"));
        assertTrue(TokenStream.isKeyword("interface"));

        // Test for invalid keywords
        assertFalse(TokenStream.isKeyword("byte1"));
        assertFalse(TokenStream.isKeyword("Char"));
        assertFalse(TokenStream.isKeyword("short1"));
        assertFalse(TokenStream.isKeyword("integer"));
        assertFalse(TokenStream.isKeyword("long_"));
        assertFalse(TokenStream.isKeyword("float1"));
        assertFalse(TokenStream.isKeyword("Double"));
        assertFalse(TokenStream.isKeyword("boolean_"));
        assertFalse(TokenStream.isKeyword("void1"));
        assertFalse(TokenStream.isKeyword("NULL"));
        assertFalse(TokenStream.isKeyword("True"));
        assertFalse(TokenStream.isKeyword("FALSE"));
        assertFalse(TokenStream.isKeyword("IFF"));
        assertFalse(TokenStream.isKeyword("elses"));
        assertFalse(TokenStream.isKeyword("whiles"));
        assertFalse(TokenStream.isKeyword("do1"));
        assertFalse(TokenStream.isKeyword("for1"));
        assertFalse(TokenStream.isKeyword("Switch"));
        assertFalse(TokenStream.isKeyword("cases"));
        assertFalse(TokenStream.isKeyword("_default"));
        assertFalse(TokenStream.isKeyword("break1"));
        assertFalse(TokenStream.isKeyword("continues"));
        assertFalse(TokenStream.isKeyword("Return"));
        assertFalse(TokenStream.isKeyword("trycatch"));
        assertFalse(TokenStream.isKeyword("Exception"));
        assertFalse(TokenStream.isKeyword("THROW"));
        assertFalse(TokenStream.isKeyword("THROWS"));
        assertFalse(TokenStream.isKeyword("nEw"));
        assertFalse(TokenStream.isKeyword("instanceOf"));
        assertFalse(TokenStream.isKeyword("Super"));
        assertFalse(TokenStream.isKeyword("stops"));
        assertFalse(TokenStream.isKeyword("implements1"));
        assertFalse(TokenStream.isKeyword("Abstract"));
        assertFalse(TokenStream.isKeyword("Final"));
        assertFalse(TokenStream.isKeyword("NAtive"));
        assertFalse(TokenStream.isKeyword("StaTic"));
        assertFalse(TokenStream.isKeyword("synchronized1"));
        assertFalse(TokenStream.isKeyword("Transient"));
        assertFalse(TokenStream.isKeyword("Volati1e"));
        assertFalse(TokenStream.isKeyword("package1"));
        assertFalse(TokenStream.isKeyword("imporT"));
        assertFalse(TokenStream.isKeyword("Class"));
        assertFalse(TokenStream.isKeyword("interface1"));
    }

    @Test
    public void testIsJSIdentifier() {
        // Test for valid identifiers
        assertTrue(TokenStream.isJSIdentifier("a"));
        assertTrue(TokenStream.isJSIdentifier("a1"));
        assertTrue(TokenStream.isJSIdentifier("_a"));
        assertTrue(TokenStream.isJSIdentifier("_a1"));
        assertTrue(TokenStream.isJSIdentifier("$a"));
        assertTrue(TokenStream.isJSIdentifier("$a1"));
        assertTrue(TokenStream.isJSIdentifier("a_b"));
        assertTrue(TokenStream.isJSIdentifier("a$b"));
        assertTrue(TokenStream.isJSIdentifier("a1_b2"));
        assertTrue(TokenStream.isJSIdentifier("_"));
        assertTrue(TokenStream.isJSIdentifier("__"));

        // Test for invalid identifiers
        assertFalse(TokenStream.isJSIdentifier("1a"));
        assertFalse(TokenStream.isJSIdentifier("1"));
        assertFalse(TokenStream.isJSIdentifier("#"));
        assertFalse(TokenStream.isJSIdentifier("@a"));
        assertFalse(TokenStream.isJSIdentifier("abc@"));
        assertFalse(TokenStream.isJSIdentifier("a&c"));
        assertFalse(TokenStream.isJSIdentifier(""));
        assertFalse(TokenStream.isJSIdentifier(" "));
        assertFalse(TokenStream.isJSIdentifier("_ "));
    }
}