package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        assertEquals("\\b", TokenMgrError.addEscapes("\b"));
        assertEquals("\\t", TokenMgrError.addEscapes("\t"));
        assertEquals("\\n", TokenMgrError.addEscapes("\n"));
        assertEquals("\\f", TokenMgrError.addEscapes("\f"));
        assertEquals("\\r", TokenMgrError.addEscapes("\r"));
        assertEquals("\\\"", TokenMgrError.addEscapes("\""));
        assertEquals("\\'", TokenMgrError.addEscapes("'"));
        assertEquals("\\\\", TokenMgrError.addEscapes("\\"));
        assertEquals("\\u0041", TokenMgrError.addEscapes("A"));
    }

    @Test
    public void testLexicalError() {
        assertEquals("Lexical error at line 1, column 1.  Encountered: '\0' (0), after : \"test\"",
                TokenMgrError.LexicalError(true, 0, 1, 1, "test", '\0'));
        assertEquals("Lexical error at line 2, column 3.  Encountered: 'c' (99), after : \"string\"",
                TokenMgrError.LexicalError(false, 2, 3, 5, "string", 'c'));
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test message", 1);
        assertEquals("Test message", error.getMessage());
    }

    @Test
    public void testConstructorNoArgs() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
    }

    @Test
    public void testConstructorWithMessage() {
        TokenMgrError error = new TokenMgrError("Test message", 1);
        assertEquals("Test message", error.getMessage());
    }

    @Test
    public void testConstructorWithDetails() {
        TokenMgrError error = new TokenMgrError(false, 2, 3, 5, "string", 'c', 0);
        assertEquals("Lexical error at line 2, column 3.  Encountered: 'c' (99), after : \"string\"", error.getMessage());
    }
}