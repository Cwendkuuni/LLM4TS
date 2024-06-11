package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        assertEquals("", TokenMgrError.addEscapes(""));
        assertEquals("\\b", TokenMgrError.addEscapes("\b"));
        assertEquals("\\t", TokenMgrError.addEscapes("\t"));
        assertEquals("\\n", TokenMgrError.addEscapes("\n"));
        assertEquals("\\f", TokenMgrError.addEscapes("\f"));
        assertEquals("\\r", TokenMgrError.addEscapes("\r"));
        assertEquals("\\\"", TokenMgrError.addEscapes("\""));
        assertEquals("\\'", TokenMgrError.addEscapes("'"));
        assertEquals("\\\\", TokenMgrError.addEscapes("\\"));
        assertEquals("\\u0020", TokenMgrError.addEscapes("\u0020"));
    }

    @Test
    public void testLexicalError() {
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 0;
        int errorColumn = 0;
        String errorAfter = "";
        char curChar = 'a';
        assertEquals("Lexical error at line 0, column 0.  Encountered: \"a\" (97), after : \"\"", TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
    }

    @Test
    public void testGetMessage() {
        TokenMgrError tme = new TokenMgrError("error", 0);
        assertEquals("error", tme.getMessage());
    }

    @Test
    public void testConstructorWithMessageAndReason() {
        TokenMgrError tme = new TokenMgrError("error", 0);
        assertEquals("error", tme.getMessage());
        assertEquals(0, tme.errorCode);
    }

    @Test
    public void testConstructorWithAllParameters() {
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 0;
        int errorColumn = 0;
        String errorAfter = "";
        char curChar = 'a';
        int reason = 0;
        TokenMgrError tme = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals("Lexical error at line 0, column 0.  Encountered: \"a\" (97), after : \"\"", tme.getMessage());
        assertEquals(0, tme.errorCode);
    }
}