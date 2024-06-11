package com.pmdesigns.jvc.tools;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String str = "Hello";
        String result = TokenMgrError.addEscapes(str);
        assertEquals("No escape characters in string", str, result);
    }

    @Test
    public void testLexicalError() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 0;
        String errorAfter = "";
        char curChar = 'a';
        String result = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals("Lexical error message checks", "Lexical error at line 1, column 0.  Encountered: <EOF>  after : \"\"", result);
    }

    @Test
    public void testGetMessage() {
        TokenMgrError tokenMgrError = new TokenMgrError("Test message", 0);
        assertEquals("Gets the message correctly", "Test message", tokenMgrError.getMessage());
    }

    @Test
    public void testNoArgConstructor() {
        TokenMgrError tokenMgrError = new TokenMgrError();
        assertEquals("No argument constructor works", null, tokenMgrError.getMessage());
    }

    @Test
    public void testTwoArgConstructor() {
        TokenMgrError tokenMgrError = new TokenMgrError("Test message", 0);
        assertEquals("Two arg constructor works", "Test message", tokenMgrError.getMessage());
        assertEquals("Error code set correctly", 0, tokenMgrError.errorCode);
    }

    @Test
    public void testFullConstructor() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 0;
        String errorAfter = "";
        char curChar = 'a';
        int reason = 0;

        TokenMgrError tokenMgrError = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals("Full constructor message set correctly", "Lexical error at line 1, column 0.  Encountered: <EOF>  after : \"\"", tokenMgrError.getMessage());
        assertEquals("Error code set correctly", 0, tokenMgrError.errorCode);
    }
}