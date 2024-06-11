package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;

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
        assertEquals("\\u0000", TokenMgrError.addEscapes("\0"));
        assertEquals("A", TokenMgrError.addEscapes("A"));
    }

    @Test
    public void testLexicalError() {
        String expected1 = "Lexical error at line 1, column 1.  Encountered: <EOF>  after : \"\"";
        assertEquals(expected1, TokenMgrError.LexicalError(true, 0, 1, 1, "", '\0'));

        String expected2 = "Lexical error at line 2, column 3.  Encountered: \"a\" (97), after : \"xyz\"";
        assertEquals(expected2, TokenMgrError.LexicalError(false, 0, 2, 3, "xyz", 'a'));
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test", 0);
        assertEquals("Test", error.getMessage());
    }

    @Test
    public void testTokenMgrError() {
        TokenMgrError error = new TokenMgrError();
    }

    @Test
    public void testTokenMgrErrorWithMessage() {
        TokenMgrError error = new TokenMgrError("Test", 0);
        assertEquals("Test", error.getMessage());
    }

    @Test
    public void testTokenMgrErrorWithParams() {
        String expected = "Lexical error at line 1, column 2.  Encountered: \"a\" (97), after : \"xyz\"";
        TokenMgrError error = new TokenMgrError(false, 0, 1, 2, "xyz", 'a', 0);
        assertEquals(expected, error.getMessage());
    }
}