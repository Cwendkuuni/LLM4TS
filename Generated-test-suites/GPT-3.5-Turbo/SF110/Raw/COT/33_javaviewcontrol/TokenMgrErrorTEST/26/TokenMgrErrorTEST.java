package com.pmdesigns.jvc.tools;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "Hello\tWorld\n";
        String expectedOutput = "Hello\\tWorld\\n";
        String result = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testLexicalError() {
        boolean EOFSeen = true;
        int lexState = TokenMgrError.LEXICAL_ERROR;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "Test";
        char curChar = '!';
        String expectedOutput = "Lexical error at line 1, column 1.  Encountered: <EOF> after : \"Test\"";
        String result = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test Error", 0);
        String expectedOutput = "Test Error";
        String result = error.getMessage();
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testTokenMgrErrorWithMessageAndReason() {
        String message = "Test Error";
        int reason = 1;
        TokenMgrError error = new TokenMgrError(message, reason);
        String expectedOutput = "Test Error";
        String result = error.getMessage();
        assertEquals(expectedOutput, result);
        assertEquals(reason, error.errorCode);
    }

    @Test
    public void testTokenMgrErrorWithParams() {
        boolean EOFSeen = true;
        int lexState = TokenMgrError.LEXICAL_ERROR;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "Test";
        char curChar = '!';
        int reason = TokenMgrError.LEXICAL_ERROR;
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String expectedOutput = "Lexical error at line 1, column 1.  Encountered: <EOF> after : \"Test\"";
        String result = error.getMessage();
        assertEquals(expectedOutput, result);
        assertEquals(reason, error.errorCode);
    }
}